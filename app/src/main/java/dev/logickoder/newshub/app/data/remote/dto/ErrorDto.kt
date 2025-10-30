package dev.logickoder.newshub.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val message: String,
)