package com.trivaris.kenmei.core.model

import com.trivaris.kenmei.auth.models.AvatarImageInformation
import com.trivaris.kenmei.auth.models.PremiumInformation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val avatar: AvatarImageInformation,
    val email: String?,
    val unconfirmedEmail: String?,
    val features: List<String>?,
    val hasEntries: Boolean?,
    val hasTags: Boolean?,
    val premium: PremiumInformation,
    val termsAccepted: Boolean?,
    @SerialName("privacy_level") val privacyLevel: String?,
    val themePreference: String?,
    val themeMode: String?
)
