package com.trivaris.kenmei.config.model

import com.trivaris.kenmei.model.domain.ConfigPremium
import com.trivaris.kenmei.model.domain.ConfigUserImage
import com.trivaris.kenmei.model.dto.LoginBannerDto
import com.trivaris.kenmei.model.dto.LoginPremiumDto
import io.ktor.http.Url
import kotlinx.serialization.Serializable

@Serializable
data class KenmeiConfig (
    val baseUrl: Url = Url("https://api.kenmei.co"),
    var userId: Long? = null,
    val username: String?,
    val avatar: ConfigUserImage?,
    val banner: ConfigUserImage?,
    val email: String?,
    val unconfirmedEmail: String?,
    val role: String?,
    val features: List<String>?,
    val hasEntries: Boolean = false,
    val hasTags: Boolean = false,
    val premium: ConfigPremium?,
    val termsAccepted: Boolean = false,
    val privacyLevel: String?,
    val themePreference: String?,
    val themeMode: String?
) {
    fun toJson(): String =
        KenmeiConfigJson.encodeToString(serializer(), this)

    companion object {
        fun fromJson(jsonString: String): KenmeiConfig =
            KenmeiConfigJson.decodeFromString(serializer(), jsonString)
    }
}