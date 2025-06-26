package com.trivaris.kenmei.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EntryLinksDto(
    @SerialName("series_url")
    val seriesUrl: String?,
    @SerialName("manga_series_url")
    val mangaSeriesUrl: String?,
)