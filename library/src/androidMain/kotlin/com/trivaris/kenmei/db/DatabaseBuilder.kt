package com.trivaris.kenmei.db

import android.content.Context
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual typealias PlatformSqlDriver = AndroidSqliteDriver

actual class DatabaseBuilder<T : Transacter> actual constructor(
    private val factory: (AndroidSqliteDriver) -> T
) {
    private var schema: SqlSchema<QueryResult.Value<Unit>>? = null
    private var store: DatabaseStore? = null
    private var path: String? = null
    private var context: Context? = null

    actual fun withSchema(schema: SqlSchema<QueryResult.Value<Unit>>): DatabaseBuilder<T> = apply {
        this.schema = schema
    }

    actual fun usingStorage(type: DatabaseStore): DatabaseBuilder<T> = apply {
        this.store = type
    }

    actual fun atPath(path: String): DatabaseBuilder<T> = apply {
        this.path = path
    }

    fun withContext(context: Context): DatabaseBuilder<T> = apply {
        this.context = context
    }

    actual fun build(): T {
        val resolvedSchema = requireNotNull(schema) { "Database schema must be set" }
        val resolvedType = requireNotNull(store) { "Storage type must be set" }

        val driver = when (resolvedType) {
            DatabaseStore.IN_MEMORY ->
                AndroidSqliteDriver(resolvedSchema, requireNotNull(context), name = null)
            DatabaseStore.PERSIST -> {
                require(!path.isNullOrBlank()) { "Database path must be set for DISK storage" }
                AndroidSqliteDriver(resolvedSchema, requireNotNull(context), name = path!!)
            }
        }

        return factory(driver)
    }
}