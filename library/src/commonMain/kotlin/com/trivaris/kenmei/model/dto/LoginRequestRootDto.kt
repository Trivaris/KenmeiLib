package com.trivaris.kenmei.model.dto

import com.trivaris.kenmei.session.models.LoginRequest
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestRootDto(
    val user: LoginRequest
)
