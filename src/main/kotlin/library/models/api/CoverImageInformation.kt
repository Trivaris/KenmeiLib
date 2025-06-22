package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.Serializable

@Serializable
data class CoverImageInformation(
    val webp: CoverImageUrls,
    val jpeg: CoverImageUrls
)