package com.trivaris.kenmei.core

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

internal typealias Schema = SqlSchema<QueryResult.Value<Unit>>

class DatabaseBuilder<T: Transacter> (
    private val factory: (JdbcSqliteDriver) -> T
) {
    private var schema: Schema? = null
    private var store: DatabaseType? = null
    private var path: String? = null

    fun storeIn(type: DatabaseType) = apply { this.store = type }
    fun path(path: String) = apply { this.path = path }
    fun schema(schema: Schema) = apply { this.schema = schema }

    fun build(): T {
        val resolvedSchema = requireNotNull(schema) { "Database shema must be set" }
        val resolvedType = requireNotNull(store) { "Database type must be set" }

        val driver = when (resolvedType) {
            DatabaseType.MEMORY -> JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            DatabaseType.PATH -> {
                require(!path.isNullOrBlank()) { "Database path must be set for PATH type" }
                val file = File(path!!)
                val driver = JdbcSqliteDriver("jdbc:sqlite:$path")
                if (!file.exists()) resolvedSchema.create(driver)
                driver
            }
        }

        return factory(driver)
    }
}
