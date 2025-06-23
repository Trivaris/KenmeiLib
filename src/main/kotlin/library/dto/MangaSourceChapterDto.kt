package com.trivaris.kenmei.library.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaSourceChapterDto(
    val id: Long,
    @SerialName("manga_source_id") val mangaSourceId : Long,
    val url: String,
    val chapterIdentifier: String,
    val volume: String? = null,
    val chapter: Double? = null,
    val title: String,
    val releasedAt: String? = null,
    val locked: Boolean? = null
) {
    fun toMangaSourceChapterInfoDto() = MangaSourceChapterInfoDto(
        id = id,
        url = url,
        chapterIdentifier = chapterIdentifier,
        volume = volume,
        chapter = chapter,
        title = title,
        releasedAt = releasedAt,
        locked = locked
    )
}