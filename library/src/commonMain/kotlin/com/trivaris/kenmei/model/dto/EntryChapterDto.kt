package com.trivaris.kenmei.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryChapterDto(
    val id: Long,
    val url: String?,
    val chapterIdentifier: String?,
    val volume: String?,
    @SerialName("chapter")
    val chapterNumber: Double?,
    val title: String?,
    val releasedAt: String?,
    val locked: Boolean,
)