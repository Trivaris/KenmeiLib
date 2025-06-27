package com.trivaris.kenmei.model.types

import kotlinx.serialization.json.Json

val KenmeiHTTPJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}