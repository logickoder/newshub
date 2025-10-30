package dev.logickoder.newshub.app.data.repository

import dev.logickoder.newshub.app.data.mapper.ArticleMapper
import dev.logickoder.newshub.app.data.remote.ApiService
import dev.logickoder.newshub.app.domain.model.ArticleType
import dev.logickoder.newshub.app.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remote: ApiService
) : NewsRepository {

    override suspend fun getArticles(
        type: ArticleType,
        page: Int,
        pageSize: Int,
        query: String?
    ) = try {
        val response = when (type) {
            is ArticleType.Headline -> remote.getHeadlines(
                pageSize = pageSize,
                page = page,
                query = query?.takeIf { it.isNotBlank() },
                category = type.category?.toString()
            )

            is ArticleType.Everything -> remote.getEverything(
                pageSize = pageSize,
                page = page,
                query = query?.takeIf { it.isNotBlank() },
                from = type.from?.toString(),
                to = type.to?.toString(),
                sortBy = type.sortBy?.toString()
            )
        }

        Result.success(response.articles.map(ArticleMapper::invoke))
    } catch (e: Exception) {
        Result.failure(e)
    }
}