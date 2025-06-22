package com.trivaris.kenmei.auth

import com.trivaris.kenmei.auth.models.JwtToken
import com.trivaris.kenmei.auth.models.LoginPayload
import com.trivaris.kenmei.auth.models.LoginRequest
import com.trivaris.kenmei.auth.models.LoginRequestWrapper
import com.trivaris.kenmei.core.KenmeiConfigProvider
import com.trivaris.kenmei.db.auth.AuthDatabase
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.slf4j.LoggerFactory

class AuthService(
    private val client: HttpClient,
    private val db: AuthDatabase
) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val config get() = KenmeiConfigProvider.instance

    fun switchUser(userId: Long) {
        db.user_sessionQueries.getByUserId(userId).executeAsOne()
        KenmeiConfigProvider.switchUser(userId)
        logger.info("Switched to user $userId")
    }

    suspend fun getAccessToken(requireValid: Boolean = true): String {
        val userId = config.userId ?: error("No active user session")
        val session = db.user_sessionQueries.getByUserId(userId).executeAsOneOrNull()
            ?: error("No session found for user $userId")

        val token = session.access_token

        if (requireValid && JwtToken.fromEncodedString(token).isExpired()) {
            val newToken = requestRenewToken(token)
            updateAuth(newToken)
            return newToken.access
        }

        return token
    }

    suspend fun login(email: String, password: String) {
        val session = config.userId
            ?.let(db.user_sessionQueries::getByUserId)
            ?.executeAsOneOrNull()

        val token = session?.access_token
        val isExpired = token?.let {
            JwtToken.fromEncodedString(it).isExpired()
        }

        val payload = when (isExpired) {
            false -> {
                logger.debug("Valid token found for userId=${config.userId}")
                null
            }
            true -> {
                logger.info("Token expired for userId=${config.userId}, attempting refresh")
                try {
                    requestRenewToken(token)
                } catch (e: ClientRequestException) {
                    logger.warn("Token refresh failed with status=${e.response.status}. Retrying login.")
                    requestNewToken(email, password)
                }
            }
            null -> {
                logger.info("No existing token found. Starting fresh login.")
                requestNewToken(email, password)
            }
        }

        if (payload == null) {
            logger.info("No auth update required.")
            return
        }

        updateAuth(payload)
    }

    private suspend fun requestRenewToken(oldToken: String): LoginPayload {
        logger.debug("Sending token refresh request.")
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

    private suspend fun requestNewToken(email: String, password: String): LoginPayload {
        logger.debug("Sending login request for email=$email")
        return client.request {
            method = HttpMethod.Post
            url {
                protocol = URLProtocol.HTTPS
                host = config.baseUrl.host
                encodedPath = "/auth/sessions"
            }
            contentType(ContentType.Application.Json)
            setBody(
                LoginRequestWrapper(
                    user = LoginRequest(
                        login = email,
                        password = password
                    )
                )
            )
            expectSuccess = true
        }.body()
    }

    private fun updateAuth(loginPayload: LoginPayload, setActive: Boolean = true) {
        val token = loginPayload.access
        val tokenInfo = JwtToken.fromEncodedString(token)

        logger.info("Updating session for user ${tokenInfo.userId}")

        db.user_sessionQueries.insertOrReplace(
            user_id = tokenInfo.userId,
            access_token = token
        )

        if (setActive) {
            KenmeiConfigProvider.switchUser(tokenInfo.userId)
            KenmeiConfigProvider.updatePreferences(loginPayload.toPreferences())
        }
    }

}