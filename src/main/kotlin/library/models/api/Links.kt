package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Links (
    @SerialName("series_url") val seriesUrl: String?,
    @SerialName("manga_series_url") val mangaSeriesUrl: String?,
)