package com.trivaris.kenmei.model.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Serializable
data class JwtToken(
    val exp: Long,
    @SerialName("user_id") val userId: Long,
    val uid: String,
    val ruid: String
) {
    fun isExpired(): Boolean {
        val now = Clock.System.now()
        val expiry = Instant.fromEpochSeconds(exp)
        return now > expiry
    }

    companion object {
        @OptIn(ExperimentalEncodingApi::class)
        fun fromEncodedString(token: String): JwtToken {
            val parts = token.split(".")
            require(parts.size >= 2) { "Invalid JWT format" }
            val decoder = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)
            val payload = decoder.decode(parts[1].encodeToByteArray()).decodeToString()
            return Json.decodeFromString<JwtToken>(payload)
        }
    }
}