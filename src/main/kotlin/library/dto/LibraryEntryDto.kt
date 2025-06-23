package com.trivaris.kenmei.library.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryEntryDto(
    val id: Long,
    @SerialName("manga_source_id") val mangaSourceId: Long,
    @SerialName("manga_series_id") val mangaSeriesId: Long,
    @SerialName("user_tag_ids") val userTagIds: List<String>?,
    val attributes: EntryAttributesDto?,
    val links: LinksDto?,
    val readChapter: ReadChapterDto?,
    val mangaSourceChapter: MangaSourceChapterInfoDto?
)
