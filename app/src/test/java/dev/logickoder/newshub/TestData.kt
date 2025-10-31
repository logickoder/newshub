package dev.logickoder.newshub

import dev.logickoder.newshub.app.data.remote.dto.ArticleDto
import dev.logickoder.newshub.app.data.remote.dto.NewsResponse
import dev.logickoder.newshub.app.data.remote.dto.SourceDto
import dev.logickoder.newshub.app.domain.model.Article
import java.net.UnknownHostException
import java.time.ZonedDateTime

object TestData {

    val testZonedDateTime: ZonedDateTime = ZonedDateTime.parse("2024-10-31T10:30:00Z")

    val testSourceDto = SourceDto(
        id = "test-source",
        name = "Test News Source"
    )

    val testArticleDto = ArticleDto(
        source = testSourceDto,
        author = "John Doe",
        title = "Test Article Title",
        description = "Test article description",
        url = "https://example.com/article",
        urlToImage = "https://example.com/image.jpg",
        publishedAt = testZonedDateTime,
        content = "This is the full content of the test article..."
    )

    val testArticleDtoWithNulls = ArticleDto(
        source = null,
        author = null,
        title = "Test Article Without Author",
        description = null,
        url = "https://example.com/article2",
        urlToImage = null,
        publishedAt = testZonedDateTime,
        content = null
    )

    val testNewsResponse = NewsResponse(
        status = "ok",
        totalResults = 2,
        articles = listOf(testArticleDto, testArticleDtoWithNulls)
    )

    val testArticle = Article(
        id = "test-article-1",
        title = "Sample News Article Title",
        description = "This is a sample description for a news article used in testing.",
        imageUrl = "https://example.com/image.jpg",
        sourceUrl = "https://example.com/article",
        sourceName = "Test News Source",
        publishedAt = testZonedDateTime,
        content = "This is the full content of the test article...",
        author = "John Doe"
    )

    val testArticleWithoutAuthor = Article(
        id = "test-article-2",
        title = "Article Without Author",
        description = "This article has no author information.",
        imageUrl = null,
        sourceUrl = "https://example.com/article2",
        sourceName = "Anonymous Source",
        publishedAt = testZonedDateTime,
        content = null,
        author = null
    )

    val testArticlesList = listOf(testArticle, testArticleWithoutAuthor)

    val testNetworkException = UnknownHostException("Network error")
}
