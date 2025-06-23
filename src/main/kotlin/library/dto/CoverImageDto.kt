package com.trivaris.kenmei.library.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoverImageDto(
    val webp: CoverImageUrlsDto,
    val jpeg: CoverImageUrlsDto
)
