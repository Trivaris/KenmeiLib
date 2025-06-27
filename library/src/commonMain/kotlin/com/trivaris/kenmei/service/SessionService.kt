package com.trivaris.kenmei.service

import co.touchlab.kermit.Logger
import com.trivaris.kenmei.model.types.JwtToken
import com.trivaris.kenmei.model.dto.LoginDto
import com.trivaris.kenmei.db.session.SessionDatabase
import com.trivaris.kenmei.model.dto.LoginRequestDto
import com.trivaris.kenmei.model.dto.LoginRequestRootDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

class SessionService(
    private val client: HttpClient,
    private val db: SessionDatabase
) {

    private val config get() = KenmeiConfigProvider.instance

    fun switchUser(userId: Long) {
        db.user_sessionQueries.getByUserId(userId).executeAsOne()
        KenmeiConfigProvider.switchUser(userId)
        Logger.i("Switched to user $userId")
    }

    suspend fun getAccessToken(requireValid: Boolean = true): String {
        val userId = config.userId ?: error("No active user session")
        val session = db.user_sessionQueries.getByUserId(userId).executeAsOneOrNull()
            ?: error("No session found for user $userId")

        val token = session.token

        if (requireValid && JwtToken.fromEncodedString(token).isExpired()) {
            val newToken = requestRenewToken(token)
            updateAuth(newToken)
            return newToken.access
        }

        return token
    }

    suspend fun login(email: String, password: String) {
        val session =
            config.userId?.let{ db.user_sessionQueries.getByUserId(it).executeAsOneOrNull() }

        val token = session?.token
        val isExpired = token?.let {
            JwtToken.fromEncodedString(it).isExpired()
        }

        val payload = when (isExpired) {
            false -> {
                Logger.d("Valid token found for userId=${config.userId}")
                null
            }
            true -> {
                Logger.i("Token expired for userId=${config.userId}, attempting refresh")
                try {
                    requestRenewToken(token)
                } catch (e: ClientRequestException) {
                    Logger.w("Token refresh failed with status=${e.response.status}. Retrying login.")
                    requestNewToken(email, password)
                }
            }
            null -> {
                Logger.i("No existing token found. Starting fresh login.")
                requestNewToken(email, password)
            }
        }

        if (payload == null) {
            Logger.i("No auth update required.")
            return
        }

        updateAuth(payload)
    }

    private suspend fun requestRenewToken(oldToken: String): LoginDto {
        Logger.d("Sending token refresh request.")
        return client.request {
            method = HttpMethod.Post
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "/refresh"
            }
            header(HttpHeaders.Authorization, "Bearer $oldToken")
            contentType(ContentType.Application.Json)
            expectSuccess = true
        }.body()
    }

    private suspend fun requestNewToken(email: String, password: String): LoginDto {
        Logger.d("Sending login request for email=$email")
        return client.request {
            method = HttpMethod.Post
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "auth/sessions"
            }
            contentType(ContentType.Application.Json)
            setBody(
                LoginRequestRootDto(
                    user = LoginRequestDto(
                        login = email,
                        password = password
                    )
                )
            )
            expectSuccess = true
        }.body()
    }

    private fun updateAuth(loginPayload: LoginDto, setActive: Boolean = true) {
        val token = loginPayload.access
        val tokenInfo = JwtToken.fromEncodedString(token)

        Logger.i("Updating session for user ${tokenInfo.userId}")

        db.user_sessionQueries.updateTokenForUser(
            token = token,
            userId = tokenInfo.userId
        )

        if (setActive) {
            KenmeiConfigProvider.switchUser(tokenInfo.userId)
            KenmeiConfigProvider.updatePreferences(loginPayload.toPreferences())
        }
    }

}