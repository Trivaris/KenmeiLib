package com.trivaris.kenmei

import com.trivaris.kenmei.db.DatabaseBuilder
import com.trivaris.kenmei.db.DatabaseStore
import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.db.session.SessionDatabase
import com.trivaris.kenmei.http.HttpClientProvider
import com.trivaris.kenmei.http.KenmeiHTTPJson
import com.trivaris.kenmei.session.models.LoginRequest
import com.trivaris.kenmei.session.models.LoginRequestWrapper
import kotlinx.coroutines.runBlocking

fun main() {
    val mangaDB = DatabaseBuilder { MangaDatabase(it) }
        .usingStorage(DatabaseStore.PERSIST)
        .withSchema(MangaDatabase.Schema)
        .atPath("data/manga.db")
        .build()

    val sessionDB = DatabaseBuilder { SessionDatabase(it) }
        .usingStorage(DatabaseStore.PERSIST)
        .withSchema(SessionDatabase.Schema)
        .atPath("data/session.db")
        .build()

    val client = KenmeiClient.create(
        HttpClientProvider.instance,
        sessionDB,
        mangaDB,
    )

    val email = System.getenv("kenmei_email")
    val password = System.getenv("kenmei_password")

    val request = LoginRequestWrapper(
        user = LoginRequest(
            login = email,
            password = password
        )
    )

    sessionDB.user_sessionQueries.updateTokenForUser(
        System.getenv("kenmei_userid").toLong(),
        System.getenv("kenmei_token")
    )

    println(KenmeiHTTPJson.encodeToString(request))

    runBlocking {
        client.auth.login(
            email,
            password
        )
        client.library.syncLibrary()
    }


}