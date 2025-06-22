package com.trivaris.kenmei.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.Base64

@Serializable
data class JwtToken(
    val exp: Long,
    @SerialName("user_id") val userId: Long,
    val uid: String,
    val ruid: String
) {
    fun isExpired(): Boolean {
        val now = Instant.now()
        val expiry = Instant.ofEpochSecond(exp)
        return now.isAfter(expiry)
    }

    companion object {
        fun fromEncodedString(token: String): JwtToken {
            val parts = token.split(".")
            require(parts.size >= 2) { "Invalid JWT format" }
            val decodedTokenPayload = String(Base64.getUrlDecoder().decode(parts[1]))
            return Json.decodeFromString<JwtToken>(decodedTokenPayload)
        }
    }
}