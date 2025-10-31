package dev.logickoder.newshub.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val status: String = "",
    val code: String = "",
    val message: String = "",
)