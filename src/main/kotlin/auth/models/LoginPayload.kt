package com.trivaris.kenmei.auth.models

import com.trivaris.kenmei.core.model.Preferences
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginPayload(
    val access: String,
    @SerialName("user_id") val userId: Int,
    val avatar: AvatarImageInformation,
    val email: String? = null,
    val unconfirmedEmail: String? = null,
    val features: List<String>? = null,
    val hasEntries: Boolean? = null,
    val hasTags: Boolean? = null,
    val premium: PremiumInformation,
    val termsAccepted: Boolean? = null,
    @SerialName("privacy_level") val privacyLevel: String? = null,
    val themePreference: String? = null,
    val themeMode: String? = null
) {
    fun toPreferences(): Preferences = Preferences(
        avatar, email, unconfirmedEmail, features, hasEntries, hasTags,
        premium, termsAccepted, privacyLevel, themePreference, themeMode
    )
}