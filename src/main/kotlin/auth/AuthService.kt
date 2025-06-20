package com.trivaris.kenmei.auth

import com.trivaris.kenmei.auth.models.LoginRequest
import com.trivaris.kenmei.auth.models.LoginRequestWrapper
import com.trivaris.kenmei.auth.models.LoginResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthService(
    private val client: HttpClient,
    private val db: AuthDatabase = AuthDatabaseProvider.instance
) {
    private val baseUrl = Url("https://api.kenmei.co")

    suspend fun login(email: String, password: String) {
        val res: LoginResponse = client.post("${baseUrl}/auth/sessions") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestWrapper(LoginRequest(email, password)))
            expectSuccess = true
        }.body()

        updateAuth(res)
    }

    internal fun updateAuth(response: LoginResponse, setActive: Boolean = true) {
        db.userSessionQueries.insertOrReplace(
            user_id = response.user_id.toLong(),
            token = response.access,
            username = response.username,
            email = response.email,
            timestamp = System.currentTimeMillis()
        )

        if (setActive)
            db.userSessionQueries.setActiveByUserId(response.user_id.toLong())
    }

    fun changeUser(userId: Long) = db.userSessionQueries.setActiveByUserId(userId)
}