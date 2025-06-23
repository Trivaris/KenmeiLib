package com.trivaris.kenmei.library.dto

import kotlinx.serialization.Serializable

@Serializable
data class LatestChapterDto(
    val id: Long,
    val url: String?,
    val chapterIdentifier: String?,
    val volume: String?,
    val chapter: Double?,
    val title: String?,
    val releasedAt: String?,
    val locked: Boolean?,
)