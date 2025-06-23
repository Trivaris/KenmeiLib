package com.trivaris.kenmei.library.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LinksDto(
    @SerialName("series_url") val seriesUrl: String?,
    @SerialName("manga_series_url") val mangaSeriesUrl: String?,
)