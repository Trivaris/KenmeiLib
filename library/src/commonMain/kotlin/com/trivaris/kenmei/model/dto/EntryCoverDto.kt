package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class EntryCoverDto(
    val webp: EntryCoverImageDto?,
    val jpeg: EntryCoverImageDto?,
)
