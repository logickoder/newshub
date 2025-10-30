package dev.logickoder.newshub.app.domain.model

import dev.logickoder.newshub.app.domain.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed interface ArticleType {
    @Serializable
    data class Headline(
        val category: ArticleCategory? = null
    ) : ArticleType

    @Serializable
    data class Everything(
        @Serializable(with = LocalDateSerializer::class)
        val from: LocalDate? = null,
        @Serializable(with = LocalDateSerializer::class)
        val to: LocalDate? = null,
        val sortBy: ArticleSortBy? = null,
    ) : ArticleType
}