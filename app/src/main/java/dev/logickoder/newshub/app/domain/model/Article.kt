package dev.logickoder.newshub.app.domain.model

import android.os.Parcelable
import dev.logickoder.newshub.app.domain.serializers.ZonedDateTimeSerializer
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Parcelize
data class Article(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val sourceUrl: String,
    val sourceName: String,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val publishedAt: ZonedDateTime,
    val content: String?,
    val author: String?,
) : Parcelable