package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginBannerImageDto(
    val large: String?,
    val small: String?,
)