package com.trivaris.kenmei.model.dto

import com.trivaris.kenmei.model.types.Score
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryAttributesDto (
    val title: String? = null,
    val cover: EntryCoverDto? = null,
    val status: Int? = null,
    val hidden: Boolean? = null,
    val favourite: Boolean? = null,
    val unread: Boolean? = null,
    val notes: String? = null,
    val score: Score? = null,
    @SerialName("last_read_at")
    val lastReadAt: String? = null,
    val createdAt: String?,
    val latestChapter: EntryChapterDto?,
    val contentRating: String?,
)