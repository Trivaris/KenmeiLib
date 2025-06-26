package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginPremiumDto(
    val tier: String?,
    val isPastDue: Boolean,
    val isActive: Boolean,
    val hasAccount: Boolean,
)