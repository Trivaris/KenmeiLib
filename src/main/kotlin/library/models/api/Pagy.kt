package com.trivaris.kenmei.library.models.api

import kotlinx.serialization.Serializable

@Serializable
data class Pagy(
    val count: Int,
    val from: Int,
    val to: Int,
    val page: Int,
    val pages: Int,
    val next: Int? = null,
    val prev: Int? = null
)