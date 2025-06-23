package com.trivaris.kenmei.library.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoverImageUrlsDto(
    val large: String,
    val small: String
)