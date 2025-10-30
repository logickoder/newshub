package dev.logickoder.newshub.app.domain

import kotlinx.serialization.json.Json

val AppJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
    explicitNulls = false
    encodeDefaults = true
}