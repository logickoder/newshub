package dev.logickoder.newshub.app.data.mapper

import dev.logickoder.newshub.app.data.remote.dto.ArticleDto
import dev.logickoder.newshub.app.domain.model.Article

object ArticleMapper {
    operator fun invoke(dto: ArticleDto): Article {
        return Article(
            id = dto.url,
            title = dto.title,
            description = dto.description.orEmpty(),
            imageUrl = dto.urlToImage,
            sourceUrl = dto.url,
            sourceName = dto.source?.name.orEmpty(),
            publishedAt = dto.publishedAt,
            author = dto.author,
            content = dto.content,
        )
    }
}