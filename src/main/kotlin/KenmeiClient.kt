package com.trivaris.kenmei

import com.trivaris.kenmei.auth.AuthService
import com.trivaris.kenmei.core.HttpClientProvider
import io.ktor.client.*


class KenmeiClient internal constructor(
    client: HttpClient = HttpClientProvider.instance,
) {
    val auth = AuthService(client)

    companion object {
        fun create(): KenmeiClient {
            val client = HttpClientProvider.instance
            return KenmeiClient(client)
        }
    }

}