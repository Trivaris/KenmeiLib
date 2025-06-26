package com.trivaris.kenmei.auth.models

import com.trivaris.kenmei.session.models.MangaEntryUpdate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MangaEntryWrapper(
    @SerialName("manga_entry") val mangaEntry: MangaEntryUpdate
)
