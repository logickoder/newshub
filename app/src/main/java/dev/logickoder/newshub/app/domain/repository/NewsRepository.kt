package dev.logickoder.newshub.app.domain.repository

import dev.logickoder.newshub.app.domain.model.Article
import dev.logickoder.newshub.app.domain.model.ArticleType

interface NewsRepository {
    suspend fun getArticles(
        type: ArticleType,
        page: Int = 1,
        pageSize: Int = 100,
        query: String? = null,
    ): Result<List<Article>>
}