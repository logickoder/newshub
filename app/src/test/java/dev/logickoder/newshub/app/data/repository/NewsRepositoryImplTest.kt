package dev.logickoder.newshub.app.data.repository

import com.google.common.truth.Truth.assertThat
import dev.logickoder.newshub.TestData
import dev.logickoder.newshub.app.data.remote.ApiService
import dev.logickoder.newshub.app.domain.model.ArticleCategory
import dev.logickoder.newshub.app.domain.model.ArticleSortBy
import dev.logickoder.newshub.app.domain.model.ArticleType
import dev.logickoder.newshub.test.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException
import java.time.LocalDate

class NewsRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockApiService = mockk<ApiService>()
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setup() {
        repository = NewsRepositoryImpl(mockApiService)
    }

    @Test
    fun `getArticles returns mapped articles on success for headlines`() = runTest {
        // Given
        val headlineType = ArticleType.Headline(category = ArticleCategory.Technology)
        coEvery {
            mockApiService.getHeadlines(any(), any())
        } returns TestData.testNewsResponse

        // When
        val result = repository.getArticles(headlineType, "test query")

        // Then
        assertThat(result.isSuccess).isTrue()
        val articles = result.getOrNull()
        assertThat(articles).hasSize(2)
        assertThat(articles?.first()?.title).isEqualTo("Test Article Title")
        assertThat(articles?.first()?.author).isEqualTo("John Doe")

        coVerify {
            mockApiService.getHeadlines(
                query = "test query",
                category = ArticleCategory.Technology.toString()
            )
        }
    }

    @Test
    fun `getArticles returns mapped articles on success for everything`() = runTest {
        // Given
        val everythingType = ArticleType.Everything(
            from = LocalDate.of(2023, 10, 1),
            to = LocalDate.of(2023, 10, 31),
            sortBy = ArticleSortBy.Popularity,
            domains = persistentListOf("techcrunch.com", "bbc.co.uk")
        )
        coEvery {
            mockApiService.getEverything(any(), any(), any(), any(), any())
        } returns TestData.testNewsResponse

        // When
        val result = repository.getArticles(everythingType, "android")

        // Then
        assertThat(result.isSuccess).isTrue()
        val articles = result.getOrNull()
        assertThat(articles).hasSize(2)

        coVerify {
            mockApiService.getEverything(
                query = "android",
                from = "2023-10-01",
                to = "2023-10-31",
                sortBy = ArticleSortBy.Popularity.toString(),
                domains = "techcrunch.com,bbc.co.uk"
            )
        }
    }

    @Test
    fun `getArticles returns error when API throws exception`() = runTest {
        // Given
        val headlineType = ArticleType.Headline()
        coEvery {
            mockApiService.getHeadlines(any(), any())
        } throws UnknownHostException("No internet connection")

        // When
        val result = repository.getArticles(headlineType, null)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(UnknownHostException::class.java)
        assertThat(result.exceptionOrNull()?.message).contains("No internet connection")
    }

    @Test
    fun `getArticles handles null and empty query correctly`() = runTest {
        // Given
        val headlineType = ArticleType.Headline()
        coEvery {
            mockApiService.getHeadlines(any(), any())
        } returns TestData.testNewsResponse

        // When - null query
        repository.getArticles(headlineType, null)

        // Then
        coVerify {
            mockApiService.getHeadlines(
                query = null,
                category = any()
            )
        }

        // When - empty query
        repository.getArticles(headlineType, "")

        // Then
        coVerify {
            mockApiService.getHeadlines(
                query = null,
                category = any()
            )
        }

        // When - blank query
        repository.getArticles(headlineType, "   ")

        // Then
        coVerify {
            mockApiService.getHeadlines(
                query = null,
                category = any()
            )
        }
    }

    @Test
    fun `getArticles handles empty domains list correctly`() = runTest {
        // Given
        val everythingType = ArticleType.Everything(
            domains = persistentListOf()
        )
        coEvery {
            mockApiService.getEverything(any(), any(), any(), any(), any())
        } returns TestData.testNewsResponse

        // When
        repository.getArticles(everythingType, "test")

        // Then
        coVerify {
            mockApiService.getEverything(
                query = "test",
                from = null,
                to = null,
                sortBy = null,
                domains = null
            )
        }
    }

    @Test
    fun `getArticles handles network timeout exception`() = runTest {
        // Given
        val headlineType = ArticleType.Headline()
        val timeoutException = java.net.SocketTimeoutException("Read timeout")
        coEvery {
            mockApiService.getHeadlines(any(), any())
        } throws timeoutException

        // When
        val result = repository.getArticles(headlineType, "test")

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(java.net.SocketTimeoutException::class.java)
    }

    @Test
    fun `getArticles maps articles with null descriptions correctly`() = runTest {
        // Given
        val headlineType = ArticleType.Headline()
        coEvery {
            mockApiService.getHeadlines(any(), any())
        } returns TestData.testNewsResponse

        // When
        val result = repository.getArticles(headlineType, null)

        // Then
        assertThat(result.isSuccess).isTrue()
        val articles = result.getOrNull()

        // First article has description
        assertThat(articles?.first()?.description).isEqualTo("Test article description")

        // Second article has null description mapped to empty string
        assertThat(articles?.get(1)?.description).isEqualTo("")
    }

    @Test
    fun `getArticles passes correct parameters for everything type with null values`() = runTest {
        // Given
        val everythingType = ArticleType.Everything(
            from = null,
            to = null,
            sortBy = null,
            domains = persistentListOf()
        )
        coEvery {
            mockApiService.getEverything(any(), any(), any(), any(), any())
        } returns TestData.testNewsResponse

        // When
        repository.getArticles(everythingType, "kotlin")

        // Then
        coVerify {
            mockApiService.getEverything(
                query = "kotlin",
                from = null,
                to = null,
                sortBy = null,
                domains = null
            )
        }
    }
}