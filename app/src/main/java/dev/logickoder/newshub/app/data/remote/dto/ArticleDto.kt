package dev.logickoder.newshub.app.data.remote.dto

import dev.logickoder.newshub.app.domain.serializers.ZonedDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class ArticleDto(
    val source: SourceDto?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val publishedAt: ZonedDateTime,
    val content: String?
)