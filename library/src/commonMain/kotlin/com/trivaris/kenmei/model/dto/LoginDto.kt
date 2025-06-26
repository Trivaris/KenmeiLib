package com.trivaris.kenmei.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val access: String,
    @SerialName("user_id")
    val userId: Long?,
    val username: String?,
    val avatar: LoginAvatarDto?,
    val banner: LoginBannerDto?,
    val email: String?,
    val unconfirmedEmail: String?,
    val role: String?,
    val features: List<String>?,
    val hasEntries: Boolean,
    val hasTags: Boolean,
    val premium: LoginPremiumDto?,
    val termsAccepted: Boolean,
    @SerialName("privacy_level")
    val privacyLevel: String?,
    val themePreference: String?,
    val themeMode: String?
)