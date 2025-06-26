package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class EntryReadChapterDto(
    val volume: Double?,
    val chapter: Double?,
    val title: String?,
)
