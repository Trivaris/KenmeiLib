package com.trivaris.kenmei.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class PremiumInformation(
    val tier: String?,
    val isPastDue: Boolean?,
    val isActive: Boolean?,
    val hasAccount: Boolean?,
)