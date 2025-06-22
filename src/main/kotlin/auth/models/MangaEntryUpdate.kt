package com.trivaris.kenmei.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaEntryUpdate(
    @SerialName("manga_source_chapter_id") val mangaSourceChapterId : Long
)