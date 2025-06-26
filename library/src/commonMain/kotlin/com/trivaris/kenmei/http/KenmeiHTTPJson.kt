package com.trivaris.kenmei.http

import kotlinx.serialization.json.Json

val KenmeiHTTPJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}