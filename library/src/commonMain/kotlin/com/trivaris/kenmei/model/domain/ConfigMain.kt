package com.trivaris.kenmei.model.domain

import com.trivaris.kenmei.model.types.KenmeiConfigJson
import io.ktor.http.Url
import kotlinx.serialization.Serializable

@Serializable
data class ConfigMain (
    val baseUrl: Url = Url("https://api.kenmei.co"),
    var userId: Long? = null,
    var preferences: ConfigUserPreferences? = null,
) {
    fun toJson(): String =
        KenmeiConfigJson.encodeToString(serializer(), this)

    companion object {
        fun fromJson(jsonString: String): ConfigMain =
            KenmeiConfigJson.decodeFromString(serializer(), jsonString)
    }
}