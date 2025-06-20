package com.trivaris.kenmei.auth

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

object AuthDatabaseProvider {
    private var dbType: DatabaseType = DatabaseType.MEMORY
    private var dbPath: String? = null
    private var _instance: AuthDatabase? = null

    val instance: AuthDatabase
        get() = _instance ?: createDatabase()

    fun setPath(path: String) {
        this.dbPath = path
    }

    fun setType(type: DatabaseType) {
        dbType = type
    }

    private fun createDatabase(): AuthDatabase {
        val driver = when (dbType) {
            DatabaseType.MEMORY -> JdbcSqliteDriver(JdbcSqliteDriver.Companion.IN_MEMORY)
            DatabaseType.PATH -> {
                requireNotNull(dbPath) { "Database path must be set when using PATH type" }
                JdbcSqliteDriver("jdbc:sqlite:$dbPath")
            }
        }

        AuthDatabase.Schema.create(driver)
        _instance = AuthDatabase(driver)
        return _instance!!
    }

    enum class DatabaseType {
        PATH,
        MEMORY
    }
}