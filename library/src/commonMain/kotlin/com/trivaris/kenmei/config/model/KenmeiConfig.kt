package com.trivaris.kenmei.config.model

import io.ktor.http.Url
import kotlinx.serialization.Serializable

@Serializable
data class KenmeiConfig (
    val baseUrl: Url = Url("https://api.kenmei.co"),
    var userId: Long? = null,
    var userPreferences: Preferences? = null
) {
    fun toJson(): String =
        KenmeiConfigJson.encodeToString(serializer(), this)

    companion object {
        fun fromJson(jsonString: String): KenmeiConfig =
            KenmeiConfigJson.decodeFromString(serializer(), jsonString)
    }
}