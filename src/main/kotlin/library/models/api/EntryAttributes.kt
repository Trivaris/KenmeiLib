package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryAttributes(
    val title: String?,
    val cover: CoverImageInformation?,
    val status: Long?,
    val hidden: Boolean?,
    val favourite: Boolean?,
    val unread: Boolean?,
    val notes: String?,
    val score: String?,
    @SerialName("last_read_at") val lastReadAt: String?,
    val createdAt: String?,
    val latestChapter: LatestChapterInformation?,
    val contentRating: String?
)