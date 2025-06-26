package com.trivaris.kenmei.model.domain

import kotlinx.serialization.Serializable

@Serializable
data class ConfigUserImage(
    val largeGifUrl: String?,
    val smallGifUrl: String?,
    val largeJpegUrl: String?,
    val smallJpegUrl: String?,
    val largeWebpUrl: String?,
    val smallWebpUrl: String?,
)