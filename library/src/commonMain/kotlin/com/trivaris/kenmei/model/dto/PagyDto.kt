package com.trivaris.kenmei.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PagyDto(
    val count: Int?,
    val from: Int?,
    val next: Int?,
    val page: Int?,
    val pages: Int?,
    val prev: Int?,
    @SerialName("scaffold_url")
    val scaffoldUrl: String?,
    val series: List<Int>?,
    val to: Int?,
    val items: Int?
)
