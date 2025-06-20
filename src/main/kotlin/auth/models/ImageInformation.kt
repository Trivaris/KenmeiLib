package com.trivaris.kenmei.auth.models

import kotlinx.serialization.Serializable

@Serializable
internal data class ImageInformation(
    val webp: ImageSize,
    val jpeg: ImageSize,
    val gif: ImageSize? = null
)

@Serializable
internal data class ImageSize(
    val large: String,
    val small: String
)