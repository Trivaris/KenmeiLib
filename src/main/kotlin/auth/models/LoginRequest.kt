package com.trivaris.kenmei.auth.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val login: String,
    val password: String,
    @SerialName("remember_me") val rememberMe: Boolean = true
)