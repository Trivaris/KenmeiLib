package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.Serializable

@Serializable
data class LibraryResponse(
    val entries: List<LibraryEntry>,
    val pagy: Pagy
)