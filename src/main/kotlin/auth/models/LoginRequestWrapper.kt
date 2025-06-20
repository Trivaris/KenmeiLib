package com.trivaris.kenmei.auth.models

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequestWrapper(
    val user: LoginRequest
)