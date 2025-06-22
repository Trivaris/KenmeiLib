package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.Serializable

@Serializable
data class MangaSourceChaptersResponse(
    val data: List<MangaSourceChapter>
)
