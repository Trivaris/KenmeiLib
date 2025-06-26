package com.trivaris.kenmei.model.domain

import kotlinx.serialization.Serializable

@Serializable
data class ConfigPremium(
    val tier: String?,
    val isPastDue: Boolean = false,
    val isActive: Boolean = false,
    val hasAccount: Boolean = false,
)