package com.trivaris.kenmei.library.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryAttributesDto(
    val title: String?,
    val cover: CoverImageDto?,
    val status: Long?,
    val hidden: Boolean?,
    val favourite: Boolean?,
    val unread: Boolean?,
    val notes: String?,
    val score: String?,
    @SerialName("last_read_at") val lastReadAt: String?,
    val createdAt: String?,
    val latestChapter: LatestChapterDto?,
    val contentRating: String?
)