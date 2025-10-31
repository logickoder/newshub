package dev.logickoder.newshub.feed

import com.google.common.truth.Truth.assertThat
import dev.logickoder.newshub.TestData
import dev.logickoder.newshub.app.domain.model.ArticleCategory
import dev.logickoder.newshub.app.domain.model.ArticleType
import dev.logickoder.newshub.app.domain.repository.NewsRepository
import dev.logickoder.newshub.feed.domain.DisplayStyle
import dev.logickoder.newshub.feed.domain.NewsFeedEvent
import dev.logickoder.newshub.test.util.MainDispatcherRule
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsFeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockRepository = mockk<NewsRepository>()
    private lateinit var viewModel: NewsFeedViewModel

    @Before
    fun setup() {
        coEvery {
            mockRepository.getArticles(any(), any())
        } returns Result.success(TestData.testArticlesList)
    }

    @Test
    fun `initial state has correct default values`() {
        // Create viewModel
        viewModel = NewsFeedViewModel(mockRepository)

        // Check initial state
        val initialState = viewModel.state.value

        assertThat(initialState.articleType).isEqualTo(ArticleType.Headline())
        assertThat(initialState.displayStyle).isEqualTo(DisplayStyle.List)
        assertThat(initialState.showFilters).isFalse()
    }

    @Test
    fun `fetchArticles loads data successfully`() = runTest {
        viewModel = NewsFeedViewModel(mockRepository)

        // Wait for initial load to complete
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.articles).isEqualTo(TestData.testArticlesList)
        assertThat(state.error).isNull()
    }

    @Test
    fun `fetchArticles handles error correctly`() = runTest {
        // Setup repository to fail
        coEvery {
            mockRepository.getArticles(any(), any())
        } returns Result.failure(TestData.testNetworkException)

        viewModel = NewsFeedViewModel(mockRepository)

        // Wait for load to complete
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.articles).isEmpty()
        assertThat(state.error).isNotNull()
    }

    @Test
    fun `toggleDisplayStyle switches between LIST and GRID`() = runTest {
        viewModel = NewsFeedViewModel(mockRepository)
        advanceUntilIdle()

        // Initial state
        assertThat(viewModel.state.value.displayStyle).isEqualTo(DisplayStyle.List)

        // Toggle to Grid
        viewModel.onEvent(NewsFeedEvent.DisplayStyleChanged(DisplayStyle.Grid))
        assertThat(viewModel.state.value.displayStyle).isEqualTo(DisplayStyle.Grid)

        // Toggle back to List
        viewModel.onEvent(NewsFeedEvent.DisplayStyleChanged(DisplayStyle.List))
        assertThat(viewModel.state.value.displayStyle).isEqualTo(DisplayStyle.List)
    }

    @Test
    fun `refresh triggers repository call`() = runTest {
        viewModel = NewsFeedViewModel(mockRepository)
        advanceUntilIdle()

        // Clear previous invocations
        clearMocks(mockRepository, answers = false)
        coEvery {
            mockRepository.getArticles(any(), any())
        } returns Result.success(TestData.testArticlesList)

        // Trigger refresh
        viewModel.onEvent(NewsFeedEvent.Refresh)
        advanceUntilIdle()

        // Verify repository was called
        coVerify(exactly = 1) {
            mockRepository.getArticles(any(), any())
        }

        assertThat(viewModel.state.value.isRefreshing).isFalse()
    }

    @Test
    fun `article type change updates state`() = runTest {
        viewModel = NewsFeedViewModel(mockRepository)
        advanceUntilIdle()

        val everythingType = ArticleType.Everything()
        viewModel.onEvent(NewsFeedEvent.ArticleTypeChanged(everythingType))

        assertThat(viewModel.state.value.articleType).isEqualTo(everythingType)
    }

    @Test
    fun `category change updates headline type`() = runTest {
        viewModel = NewsFeedViewModel(mockRepository)
        advanceUntilIdle()

        viewModel.onEvent(NewsFeedEvent.CategoryChanged(ArticleCategory.Technology))

        val articleType = viewModel.state.value.articleType as ArticleType.Headline
        assertThat(articleType.category).isEqualTo(ArticleCategory.Technology)
    }

    @Test
    fun `toggle filters changes showFilters state`() = runTest {
        viewModel = NewsFeedViewModel(mockRepository)
        advanceUntilIdle()

        // Initially false
        assertThat(viewModel.state.value.showFilters).isFalse()

        // Toggle to true
        viewModel.onEvent(NewsFeedEvent.ToggleFilters)
        assertThat(viewModel.state.value.showFilters).isTrue()

        // Toggle back to false
        viewModel.onEvent(NewsFeedEvent.ToggleFilters)
        assertThat(viewModel.state.value.showFilters).isFalse()
    }

    @Test
    fun `clear error removes error from state`() = runTest {
        // Setup to fail first
        coEvery {
            mockRepository.getArticles(any(), any())
        } returns Result.failure(TestData.testNetworkException)

        viewModel = NewsFeedViewModel(mockRepository)
        advanceUntilIdle()

        // Verify error exists
        assertThat(viewModel.state.value.error).isNotNull()

        // Clear error
        viewModel.onEvent(NewsFeedEvent.ClearError)

        // Verify error is cleared
        assertThat(viewModel.state.value.error).isNull()
    }
}