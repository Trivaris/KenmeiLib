package com.trivaris.kenmei.config.model

import kotlinx.serialization.json.Json

val KenmeiConfigJson = Json {
    prettyPrint = true
    encodeDefaults = true
    explicitNulls = true
}