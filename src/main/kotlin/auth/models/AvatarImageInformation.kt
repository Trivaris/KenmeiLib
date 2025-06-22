package com.trivaris.kenmei.auth.models

import kotlinx.serialization.Serializable

@Serializable
data class AvatarImageInformation(
    var gif: AvatarImageUrls,
    var jpeg: AvatarImageUrls,
    var webp: AvatarImageUrls
)