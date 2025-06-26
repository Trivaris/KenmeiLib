package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginAvatarImageDto(
    val large: String?,
    val small: String?,
)