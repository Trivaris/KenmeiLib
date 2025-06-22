package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LibraryEntry(
    val id: Long,
    @SerialName("manga_source_id") val mangaSourceId: Long,
    @SerialName("manga_series_id") val mangaSeriesId: Long,
    @SerialName("user_tag_ids") val userTagIds: List<String>?,
    val attributes: EntryAttributes?,
    val links: Links?,
    val readChapter: ReadChapterInformation?,
    val mangaSourceChapter: MangaSourceChapterInformation?
)
