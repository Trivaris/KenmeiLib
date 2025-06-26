package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class EntryReadChapterDto(
    val volume: Long?,
    val chapter: Long?,
    val title: String?,
)
