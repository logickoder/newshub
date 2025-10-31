package dev.logickoder.newshub.app.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.logickoder.newshub.TestData
import dev.logickoder.newshub.app.data.remote.dto.ArticleDto
import dev.logickoder.newshub.app.data.remote.dto.SourceDto
import org.junit.Test
import java.time.ZonedDateTime

class ArticleMapperTest {

    @Test
    fun `maps ArticleDto to Article correctly with all fields`() {
        // Given
        val articleDto = TestData.testArticleDto

        // When
        val article = ArticleMapper(articleDto)

        // Then
        assertThat(article.id).isEqualTo(articleDto.url)
        assertThat(article.title).isEqualTo(articleDto.title)
        assertThat(article.description).isEqualTo(articleDto.description)
        assertThat(article.imageUrl).isEqualTo(articleDto.urlToImage)
        assertThat(article.sourceUrl).isEqualTo(articleDto.url)
        assertThat(article.sourceName).isEqualTo(articleDto.source?.name)
        assertThat(article.publishedAt).isEqualTo(articleDto.publishedAt)
        assertThat(article.author).isEqualTo(articleDto.author)
        assertThat(article.content).isEqualTo(articleDto.content)
    }

    @Test
    fun `handles null author gracefully`() {
        // Given
        val articleDto = TestData.testArticleDtoWithNulls

        // When
        val article = ArticleMapper(articleDto)

        // Then
        assertThat(article.author).isNull()
        assertThat(article.title).isEqualTo("Test Article Without Author")
    }

    @Test
    fun `handles null description gracefully`() {
        // Given
        val articleDto = TestData.testArticleDtoWithNulls

        // When
        val article = ArticleMapper(articleDto)

        // Then
        assertThat(article.description).isEmpty()
    }

    @Test
    fun `handles null source gracefully`() {
        // Given
        val articleDto = TestData.testArticleDtoWithNulls

        // When
        val article = ArticleMapper(articleDto)

        // Then
        assertThat(article.sourceName).isEmpty()
    }

    @Test
    fun `handles null imageUrl gracefully`() {
        // Given
        val articleDto = TestData.testArticleDtoWithNulls

        // When
        val article = ArticleMapper(articleDto)

        // Then
        assertThat(article.imageUrl).isNull()
    }

    @Test
    fun `handles null content gracefully`() {
        // Given
        val articleDto = TestData.testArticleDtoWithNulls

        // When
        val article = ArticleMapper(articleDto)

        // Then
        assertThat(article.content).isNull()
    }

    @Test
    fun `generates consistent id from URL`() {
        // Given
        val url1 = "https://example.com/article/123"
        val url2 = "https://another.com/news/456"

        val articleDto1 = ArticleDto(
            source = TestData.testSourceDto,
            author = "Author 1",
            title = "Title 1",
            description = "Description 1",
            url = url1,
            urlToImage = null,
            publishedAt = TestData.testZonedDateTime,
            content = null
        )

        val articleDto2 = ArticleDto(
            source = TestData.testSourceDto,
            author = "Author 2",
            title = "Title 2",
            description = "Description 2",
            url = url2,
            urlToImage = null,
            publishedAt = TestData.testZonedDateTime,
            content = null
        )

        // When
        val article1 = ArticleMapper(articleDto1)
        val article2 = ArticleMapper(articleDto2)

        // Then
        assertThat(article1.id).isEqualTo(url1)
        assertThat(article2.id).isEqualTo(url2)
        assertThat(article1.id).isNotEqualTo(article2.id)
    }

    @Test
    fun `same URL produces same id consistently`() {
        // Given
        val sameUrl = "https://example.com/article/123"

        val articleDto1 = ArticleDto(
            source = TestData.testSourceDto,
            author = "Different Author 1",
            title = "Different Title 1",
            description = "Different Description 1",
            url = sameUrl,
            urlToImage = "different-image1.jpg",
            publishedAt = TestData.testZonedDateTime,
            content = "Different content 1"
        )

        val articleDto2 = ArticleDto(
            source = SourceDto(id = "different-source", name = "Different Source"),
            author = "Different Author 2",
            title = "Different Title 2",
            description = "Different Description 2",
            url = sameUrl,
            urlToImage = "different-image2.jpg",
            publishedAt = ZonedDateTime.parse("2023-11-30T15:30:00Z"),
            content = "Different content 2"
        )

        // When
        val article1 = ArticleMapper(articleDto1)
        val article2 = ArticleMapper(articleDto2)

        // Then
        assertThat(article1.id).isEqualTo(article2.id)
        assertThat(article1.id).isEqualTo(sameUrl)
    }

    @Test
    fun `maps all required fields correctly`() {
        // Given
        val testDate = ZonedDateTime.parse("2023-12-01T12:00:00Z")
        val articleDto = ArticleDto(
            source = SourceDto(id = "test-id", name = "Test News"),
            author = "Jane Smith",
            title = "Breaking News Title",
            description = "This is a test description for breaking news",
            url = "https://testnews.com/breaking-news-123",
            urlToImage = "https://testnews.com/images/breaking.jpg",
            publishedAt = testDate,
            content = "Full content of the breaking news article..."
        )

        // When
        val article = ArticleMapper(articleDto)

        // Then
        with(article) {
            assertThat(id).isEqualTo("https://testnews.com/breaking-news-123")
            assertThat(title).isEqualTo("Breaking News Title")
            assertThat(description).isEqualTo("This is a test description for breaking news")
            assertThat(imageUrl).isEqualTo("https://testnews.com/images/breaking.jpg")
            assertThat(sourceUrl).isEqualTo("https://testnews.com/breaking-news-123")
            assertThat(sourceName).isEqualTo("Test News")
            assertThat(publishedAt).isEqualTo(testDate)
            assertThat(author).isEqualTo("Jane Smith")
            assertThat(content).isEqualTo("Full content of the breaking news article...")
        }
    }

    @Test
    fun `empty source name is handled correctly`() {
        // Given
        val articleDto = ArticleDto(
            source = SourceDto(id = "test-id", name = ""),
            author = "Test Author",
            title = "Test Title",
            description = "Test Description",
            url = "https://test.com",
            urlToImage = null,
            publishedAt = TestData.testZonedDateTime,
            content = null
        )

        // When
        val article = ArticleMapper(articleDto)

        // Then
        assertThat(article.sourceName).isEmpty()
    }
}