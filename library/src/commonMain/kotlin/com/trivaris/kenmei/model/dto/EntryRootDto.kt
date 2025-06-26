package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class EntryRootDto(
    val entries: List<EntryDto>?,
    val pagy: PagyDto?
)
