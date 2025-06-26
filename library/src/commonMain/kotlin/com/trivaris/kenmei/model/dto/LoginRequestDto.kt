package com.trivaris.kenmei.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val login: String,
    val password: String,
    @SerialName("remember_me")
    val rememberMe: Boolean = true
)
