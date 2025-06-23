package com.trivaris.kenmei.library.dto

import kotlinx.serialization.Serializable

@Serializable
data class MangaSourceChaptersResponseDto(
    val data: List<MangaSourceChapterDto>
)
