package com.trivaris.kenmei.db

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import co.touchlab.kermit.Logger
import java.io.File

actual typealias PlatformSqlDriver = JdbcSqliteDriver

actual class DatabaseBuilder<T : Transacter> actual constructor(
    private val factory: (driver: PlatformSqlDriver) -> T
) {
    private var schema: SqlSchema<QueryResult.Value<Unit>>? = null
    private var store: DatabaseStore? = null
    private var path: String? = null

    actual fun withSchema(schema: SqlSchema<QueryResult.Value<Unit>>): DatabaseBuilder<T> = apply {
        this.schema = schema
    }

    actual fun usingStorage(type: DatabaseStore): DatabaseBuilder<T> = apply {
        this.store = type
    }

    actual fun atPath(path: String): DatabaseBuilder<T> = apply {
        this.path = path
    }

    actual fun build(): T {
        val resolvedSchema = requireNotNull(schema) { "Database schema must be set" }
        val resolvedType = requireNotNull(store) { "Database type must be set" }

        val driver = when (resolvedType) {
            DatabaseStore.IN_MEMORY -> JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            DatabaseStore.PERSIST -> {
                require(!path.isNullOrBlank()) { "Database path must be set for PATH type" }
                Logger.i("Hello World")
                val driver = JdbcSqliteDriver("jdbc:sqlite:$path")
                Logger.i("${resolvedSchema}")
                resolvedSchema.create(driver)
                driver
            }
        }

        return factory(driver)
    }
}