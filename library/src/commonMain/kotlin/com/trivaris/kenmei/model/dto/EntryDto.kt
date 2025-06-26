package com.trivaris.kenmei.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryDto(
    val id: Long,
    @SerialName("manga_source_id")
    val mangaSourceId: Long?,
    @SerialName("manga_series_id")
    val mangaSeriesId: Long?,
    @SerialName("user_tag_ids")
    val userTagIds: List<Long>?,
    val attributes: EntryAttributesDto?,
    val links: EntryLinksDto?,
    val readChapter: EntryReadChapterDto?,
    val mangaSourceChapter: EntryChapterDto?,

    )

