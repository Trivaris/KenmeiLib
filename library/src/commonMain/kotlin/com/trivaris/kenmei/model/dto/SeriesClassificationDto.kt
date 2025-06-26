package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class SeriesClassificationDto(
    val id: Long?,
    val category: String?,
    val name: String?,
)