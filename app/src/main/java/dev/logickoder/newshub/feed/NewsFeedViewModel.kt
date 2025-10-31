package dev.logickoder.newshub.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logickoder.newshub.app.data.mapper.ErrorMapper
import dev.logickoder.newshub.app.domain.model.ArticleType
import dev.logickoder.newshub.app.domain.repository.NewsRepository
import dev.logickoder.newshub.feed.domain.NewsFeedEvent
import dev.logickoder.newshub.feed.domain.NewsFeedState
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class NewsFeedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NewsFeedState())
    val state: StateFlow<NewsFeedState> = _state.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    /**
     * Used to track whether the article type has changed while the filters panel is open
     */
    private var articleType: ArticleType? = null

    private var loadJob: Job? = null

    init {
        snapshotFlow { searchQuery }
            .debounce(500L)
            .distinctUntilChanged()
            .onEach { loadArticles() }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: NewsFeedEvent) {
        when (event) {
            is NewsFeedEvent.SearchQueryChanged -> searchQuery = event.query

            is NewsFeedEvent.ArticleTypeChanged -> _state.update {
                it.copy(articleType = event.type)
            }

            is NewsFeedEvent.DisplayStyleChanged -> _state.update {
                it.copy(displayStyle = event.style)
            }

            is NewsFeedEvent.CategoryChanged -> _state.update {
                when (val type = it.articleType) {
                    is ArticleType.Everything -> it
                    is ArticleType.Headline -> it.copy(
                        articleType = type.copy(
                            category = event.category
                        )
                    )
                }
            }

            is NewsFeedEvent.FromDateChanged -> _state.update {
                when (val type = it.articleType) {
                    is ArticleType.Headline -> it
                    is ArticleType.Everything -> it.copy(
                        articleType = type.copy(
                            from = event.date
                        )
                    )
                }
            }

            is NewsFeedEvent.ToDateChanged -> _state.update {
                when (val type = it.articleType) {
                    is ArticleType.Headline -> it
                    is ArticleType.Everything -> it.copy(
                        articleType = type.copy(
                            to = event.date
                        )
                    )
                }
            }

            is NewsFeedEvent.SortByChanged -> _state.update {
                when (val type = it.articleType) {
                    is ArticleType.Headline -> it
                    is ArticleType.Everything -> it.copy(
                        articleType = type.copy(
                            sortBy = event.sortBy
                        )
                    )
                }
            }

            is NewsFeedEvent.DomainsChanged -> _state.update {
                when (val type = it.articleType) {
                    is ArticleType.Headline -> it
                    is ArticleType.Everything -> it.copy(
                        articleType = type.copy(
                            domains = event.domains
                        )
                    )
                }
            }

            NewsFeedEvent.ToggleFilters -> _state.update {
                when (it.showFilters) {
                    false -> articleType = it.articleType
                    // fetch articles only if the type has changed while filters was open
                    else -> if (it.articleType != articleType) {
                        loadArticles()
                    }
                }
                it.copy(showFilters = !it.showFilters)
            }

            NewsFeedEvent.Refresh -> {
                _state.update { it.copy(isRefreshing = true) }
                loadArticles()
            }

            NewsFeedEvent.ClearError -> _state.update {
                it.copy(error = null)
            }

            NewsFeedEvent.LoadArticles -> loadArticles()
        }
    }

    private fun loadArticles() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = !it.isRefreshing) }

            val result = newsRepository.getArticles(
                type = _state.value.articleType,
                query = searchQuery
            )

            if (!coroutineContext.isActive) {
                return@launch
            }

            result.fold(
                onSuccess = { newArticles ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            articles = newArticles.toImmutableList(),
                            error = null,
                        )
                    }
                },
                onFailure = { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = ErrorMapper(exception)
                        )
                    }

                    Napier.e(exception) { "Failed to fetch articles" }
                }
            )
        }
    }

    companion object {
        val availableDomains = listOf(
            "bbc.co.uk", "techcrunch.com", "engadget.com", "cnn.com",
            "reuters.com", "theverge.com", "arstechnica.com", "wired.com"
        )
    }
}
