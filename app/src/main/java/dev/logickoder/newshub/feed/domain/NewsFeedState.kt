package dev.logickoder.newshub.feed.domain

import dev.logickoder.newshub.app.domain.model.Article
import dev.logickoder.newshub.app.domain.model.ArticleType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NewsFeedState(
    val articles: ImmutableList<Article> = persistentListOf(),
    val isLoading: Boolean = false,
    val articleType: ArticleType = ArticleType.Headline(),
    val displayStyle: DisplayStyle = DisplayStyle.List,
    val isRefreshing: Boolean = false,
    val showFilters: Boolean = false,
    val error: String? = null,
)