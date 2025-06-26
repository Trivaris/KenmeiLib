package com.trivaris.kenmei

import com.trivaris.kenmei.db.manga.MangaDatabase
import com.trivaris.kenmei.session.SessionService
import com.trivaris.kenmei.http.HttpClientProvider
import com.trivaris.kenmei.db.session.SessionDatabase
import com.trivaris.kenmei.manga.MangaService
import io.ktor.client.*


class KenmeiClient internal constructor(
    client: HttpClient,
    sessionDB: SessionDatabase,
    mangaDB: MangaDatabase
) {
    val auth = SessionService(client, sessionDB)
    val library = MangaService(client, mangaDB, auth)

    companion object {
        fun create(
            client: HttpClient = HttpClientProvider.instance,
            sessionDB: SessionDatabase,
            mangaDB: MangaDatabase
        ): KenmeiClient =
            KenmeiClient(
                client,
                sessionDB,
                mangaDB
            )
    }

}