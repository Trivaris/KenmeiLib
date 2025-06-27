package com.trivaris.kenmei.model.domain

import kotlinx.serialization.Serializable

@Serializable
data class ConfigUserPreferences(
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
)