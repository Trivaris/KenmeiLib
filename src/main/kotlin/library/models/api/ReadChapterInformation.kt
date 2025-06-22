package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.Serializable

@Serializable
data class ReadChapterInformation(
    val volume: String?,
    val chapter: Double?,
    val title: String?,
)