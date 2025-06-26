package com.trivaris.kenmei.session.models

import kotlinx.serialization.Serializable

@Serializable
data class AvatarImageUrls(
    val large: String?,
    val small: String?,
)