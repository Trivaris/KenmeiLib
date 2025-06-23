package com.trivaris.kenmei.library.dto

import kotlinx.serialization.Serializable

@Serializable
data class LibraryResponseDto(
    val entries: List<LibraryEntryDto>,
    val pagy: PagyDto
)
