package com.trivaris.kenmei.model.types

import kotlinx.serialization.json.Json

val KenmeiConfigJson = Json {
    prettyPrint = true
    encodeDefaults = true
    explicitNulls = true
}