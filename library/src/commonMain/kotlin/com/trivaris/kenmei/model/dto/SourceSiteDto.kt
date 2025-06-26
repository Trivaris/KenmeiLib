package com.trivaris.kenmei.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceSiteDto (
    val id: Long,
    val name: String?,
    val src: String?,
    @SerialName("site_type")
    val siteType: String?,
    val online: Boolean,
    val disabled: Boolean,
)
