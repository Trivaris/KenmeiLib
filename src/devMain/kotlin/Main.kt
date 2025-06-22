package com.trivaris.kenmei

import com.trivaris.kenmei.core.DatabaseBuilder
import com.trivaris.kenmei.core.DatabaseType
import com.trivaris.kenmei.db.auth.AuthDatabase
import com.trivaris.kenmei.db.library.LibraryDatabase
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

fun main()  {
    val logger = LoggerFactory.getLogger("Main")

    val authdb = DatabaseBuilder { driver -> AuthDatabase(driver) }
        .storeIn(DatabaseType.PATH)
        .schema(AuthDatabase.Schema)
        .path("data/auth.db")
        .build()

    val librarydb = DatabaseBuilder { driver -> LibraryDatabase(driver) }
        .storeIn(DatabaseType.PATH)
        .schema(LibraryDatabase.Schema)
        .path("data/lib.db")
        .build()

    val client = KenmeiClient.create(
        authdb = authdb,
        librarydb =  librarydb
    )

    val token = authdb.user_sessionQueries.getByUserId(29347).executeAsOne().access_token
    logger.info("Found token=$token")

    val email = System.getenv("KENMEI_EMAIL")
        ?: error("KENMEI_EMAIL environment variable not set")

    val password = System.getenv("KENMEI_PASSWORD")
        ?: error("KENMEI_PASSWORD environment variable not set")

    runBlocking {
        client.auth.login(
            email = email,
            password = password
        )
        client.library.syncLibrary()
    }

}