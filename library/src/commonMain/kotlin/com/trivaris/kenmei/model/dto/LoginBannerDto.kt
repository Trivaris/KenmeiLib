package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginBannerDto(
    var gif: LoginBannerImageDto?,
    var jpeg: LoginBannerImageDto?,
    var webp: LoginBannerImageDto?
)