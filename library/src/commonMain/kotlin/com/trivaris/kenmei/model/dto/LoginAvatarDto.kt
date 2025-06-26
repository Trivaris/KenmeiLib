package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginAvatarDto(
    var gif: LoginAvatarImageDto?,
    var jpeg: LoginAvatarImageDto?,
    var webp: LoginAvatarImageDto?
)