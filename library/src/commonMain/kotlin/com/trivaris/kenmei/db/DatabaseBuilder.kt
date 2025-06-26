package com.trivaris.kenmei.db

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlSchema

internal typealias Schema = SqlSchema<QueryResult.Value<Unit>>

expect class DatabaseBuilder<T : Transacter>(
    factory: (driver: PlatformSqlDriver) -> T
) {
    fun withSchema(schema: Schema): DatabaseBuilder<T>
    fun usingStorage(type: DatabaseStore): DatabaseBuilder<T>
    fun atPath(path: String): DatabaseBuilder<T>
    fun build(): T
}