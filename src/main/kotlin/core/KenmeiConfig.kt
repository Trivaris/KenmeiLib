package com.trivaris.kenmei.core

import com.trivaris.kenmei.core.model.Preferences
import io.ktor.http.Url
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private val json = Json {
    prettyPrint = true
    encodeDefaults = true
    explicitNulls = true
}

@Serializable
data class KenmeiConfig (
    val baseUrl: Url = Url("https://api.kenmei.co"),
    var userId: Long? = null,
    var userPreferences: Preferences? = null
) {
    fun toJson(): String =
        json.encodeToString(serializer(), this)

    companion object {
        fun fromJson(jsonString: String): KenmeiConfig =
            json.decodeFromString(serializer(), jsonString)
    }
}