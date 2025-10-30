package dev.logickoder.newshub.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SourceDto(
    val id: String,
    val name: String
)