package com.trivaris.kenmei.session.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestWrapper(
    val user: LoginRequest
)

@Serializable
data class LoginRequest(
    val login: String,
    val password: String,
    @SerialName("remember_me")
    val rememberMe: Boolean = true
)