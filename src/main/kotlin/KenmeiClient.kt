package com.trivaris.kenmei

import com.trivaris.kenmei.auth.AuthService
import com.trivaris.kenmei.core.HttpClientProvider
import com.trivaris.kenmei.db.auth.AuthDatabase
import com.trivaris.kenmei.db.library.LibraryDatabase
import com.trivaris.kenmei.library.LibraryService
import io.ktor.client.*


class KenmeiClient internal constructor(
    client: HttpClient,
    authdb: AuthDatabase,
    librarydb: LibraryDatabase
) {
    val auth = AuthService(client, authdb)
    val library = LibraryService(client, librarydb, auth)

    companion object {
        fun create(
            client: HttpClient = HttpClientProvider.instance,
            authdb: AuthDatabase,
            librarydb: LibraryDatabase
        ): KenmeiClient =
            KenmeiClient(
                client,
                authdb,
                librarydb
            )
    }

}