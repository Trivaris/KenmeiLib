package com.trivaris.kenmei.library.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagyDto(
    val count: Int,
    val from: Int,
    val to: Int,
    val page: Int,
    val pages: Int,
    val next: Int? = null,
    val prev: Int? = null
)