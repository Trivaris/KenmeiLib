package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.Serializable

@Serializable
data class CoverImageUrls (
    val large: String,
    val small: String
)