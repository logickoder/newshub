package dev.logickoder.newshub.app.domain.model

import java.time.ZonedDateTime

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val sourceUrl: String,
    val sourceName: String,
    val publishedAt: ZonedDateTime,
    val content: String?,
    val author: String?,
)