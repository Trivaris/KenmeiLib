package com.trivaris.kenmei.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserTagDto(
    val id: Long,
    val name: String?,
    val description: String?,
    val entryCount: Long?,
)
