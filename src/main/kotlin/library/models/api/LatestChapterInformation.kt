package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.Serializable

@Serializable
data class LatestChapterInformation(
    val id: Long,
    val url: String?,
    val chapterIdentifier: String?,
    val volume: String?,
    val chapter: Double?,
    val title: String?,
    val releasedAt: String?,
    val locked: Boolean?,
)