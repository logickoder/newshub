package dev.logickoder.newshub.feed.domain

import dev.logickoder.newshub.app.domain.model.ArticleCategory
import dev.logickoder.newshub.app.domain.model.ArticleSortBy
import dev.logickoder.newshub.app.domain.model.ArticleType
import java.time.LocalDate

sealed interface NewsFeedEvent {
    data class SearchQueryChanged(val query: String) : NewsFeedEvent
    data class ArticleTypeChanged(val type: ArticleType) : NewsFeedEvent
    data class DisplayStyleChanged(val style: DisplayStyle) : NewsFeedEvent
    data class CategoryChanged(val category: ArticleCategory) : NewsFeedEvent
    data class FromDateChanged(val date: LocalDate?) : NewsFeedEvent
    data class ToDateChanged(val date: LocalDate?) : NewsFeedEvent
    data class SortByChanged(val sortBy: ArticleSortBy?) : NewsFeedEvent
    data class DomainsChanged(val domains: List<String>) : NewsFeedEvent
    data object ToggleFilters : NewsFeedEvent
    data object Refresh : NewsFeedEvent
    data object LoadArticles : NewsFeedEvent
    data object ClearError : NewsFeedEvent
}