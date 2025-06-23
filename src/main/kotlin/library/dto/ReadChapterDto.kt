package com.trivaris.kenmei.library.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReadChapterDto(
    val volume: String?,
    val chapter: Double?,
    val title: String?,
)