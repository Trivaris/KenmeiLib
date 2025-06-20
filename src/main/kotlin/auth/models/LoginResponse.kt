package com.trivaris.kenmei.auth.models

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginResponse(
    val access: String,
    val user_id: Int,
    val username: String,
    val avatar: ImageInformation,
    val banner: ImageInformation,
    val email: String,
    val unconfirmedEmail: String? = null,
    val role: String,
    val features: List<String> = emptyList(),
    val hasEntries: Boolean,
    val hasTags: Boolean,
    val premium: PremiumInformation,
    val termsAccepted: Boolean,
    val privacy_level: String,
    val themePreference: String,
    val themeMode: String
)