package dev.logickoder.newshub.feed

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.logickoder.newshub.R
import dev.logickoder.newshub.app.domain.model.Article
import dev.logickoder.newshub.app.theme.AppTheme
import dev.logickoder.newshub.app.theme.LocalAppColors
import dev.logickoder.newshub.feed.components.ArticleGridItem
import dev.logickoder.newshub.feed.components.ArticleGridItemShimmer
import dev.logickoder.newshub.feed.components.ArticleListItem
import dev.logickoder.newshub.feed.components.ArticleListItemShimmer
import dev.logickoder.newshub.feed.components.FilterSection
import dev.logickoder.newshub.feed.components.SearchBar
import dev.logickoder.newshub.feed.domain.DisplayStyle
import dev.logickoder.newshub.feed.domain.NewsFeedEvent
import dev.logickoder.newshub.feed.domain.NewsFeedState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen(
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewsFeedViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    NewsFeedScreenContent(
        state = state,
        query = viewModel.searchQuery,
        modifier = modifier,
        onArticleClick = onArticleClick,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun NewsFeedScreenContent(
    state: NewsFeedState,
    query: String,
    modifier: Modifier = Modifier,
    onArticleClick: (Article) -> Unit,
    onEvent: (NewsFeedEvent) -> Unit,
) {
    val colors = LocalAppColors.current

    val pullToRefreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()
    val gridState = rememberLazyGridState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                displayStyle = state.displayStyle,
                showFilters = state.showFilters,
                onEvent = onEvent
            )
        },
        containerColor = colors.background,
        content = { paddingValues ->
            PullToRefreshBox(
                state = pullToRefreshState,
                isRefreshing = state.isRefreshing,
                onRefresh = {
                    onEvent(NewsFeedEvent.Refresh)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                content = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        content = {
                            SearchBar(
                                query = query,
                                onQueryChange = { query ->
                                    onEvent(NewsFeedEvent.SearchQueryChanged(query))
                                },
                                modifier = Modifier.padding(16.dp)
                            )

                            FilterSection(
                                articleType = state.articleType,
                                isVisible = state.showFilters,
                                onArticleTypeChanged = { type ->
                                    onEvent(NewsFeedEvent.ArticleTypeChanged(type))
                                },
                                onCategoryChanged = { category ->
                                    onEvent(NewsFeedEvent.CategoryChanged(category))
                                },
                                onFromDateChanged = { date ->
                                    onEvent(NewsFeedEvent.FromDateChanged(date))
                                },
                                onToDateChanged = { date ->
                                    onEvent(NewsFeedEvent.ToDateChanged(date))
                                },
                                onSortByChanged = { sortBy ->
                                    onEvent(NewsFeedEvent.SortByChanged(sortBy))
                                },
                                onDomainsChanged = { domains ->
                                    onEvent(NewsFeedEvent.DomainsChanged(domains))
                                },
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            if (state.showFilters) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                content = {
                                    when {
                                        state.articles.isEmpty() && !state.isLoading && !state.isRefreshing -> EmptyState(
                                            modifier = Modifier.align(Alignment.Center)
                                        )

                                        else -> ArticlesList(
                                            modifier = Modifier.fillMaxSize(),
                                            isLoading = state.isLoading && state.articles.isEmpty(),
                                            articles = state.articles,
                                            displayStyle = state.displayStyle,
                                            listState = listState,
                                            gridState = gridState,
                                            onArticleClick = onArticleClick,
                                        )
                                    }
                                }
                            )
                        }

                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    displayStyle: DisplayStyle,
    showFilters: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (NewsFeedEvent) -> Unit,
) {
    val colors = LocalAppColors.current

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = colors.onSurface,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(
                onClick = {
                    onEvent(
                        NewsFeedEvent.DisplayStyleChanged(
                            when (displayStyle) {
                                DisplayStyle.List -> DisplayStyle.Grid
                                DisplayStyle.Grid -> DisplayStyle.List
                            }
                        )
                    )
                },
                content = {
                    Icon(
                        modifier = Modifier.animateContentSize(),
                        imageVector = when (displayStyle) {
                            DisplayStyle.List -> Icons.Default.GridView
                            DisplayStyle.Grid -> Icons.AutoMirrored.Filled.ViewList
                        },
                        contentDescription = "Toggle display style",
                        tint = colors.onSurface
                    )
                }
            )

            IconButton(
                onClick = {
                    onEvent(NewsFeedEvent.ToggleFilters)
                },
                content = {
                    val tint by animateColorAsState(
                        when (showFilters) {
                            true -> colors.primary
                            false -> colors.onSurface
                        }
                    )
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Toggle filters",
                        tint = tint
                    )
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.surface
        )
    )
}

@Composable
private fun ArticlesList(
    isLoading: Boolean,
    articles: ImmutableList<Article>,
    displayStyle: DisplayStyle,
    listState: LazyListState,
    gridState: LazyGridState,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentPadding = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        bottom = 16.dp
    )
    when (displayStyle) {
        DisplayStyle.List -> LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                when (isLoading) {
                    true -> items(5) {
                        ArticleListItemShimmer(Modifier.animateItem())
                    }

                    else -> items(
                        items = articles,
                        key = { article -> article.id },
                        itemContent = { article ->
                            ArticleListItem(
                                modifier = Modifier.animateItem(),
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    )
                }
            }
        )

        DisplayStyle.Grid -> LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            state = gridState,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                when (isLoading) {
                    true -> items(6) {
                        ArticleGridItemShimmer(Modifier.animateItem())
                    }

                    else -> items(
                        items = articles,
                        key = { article -> article.id },
                        itemContent = { article ->
                            ArticleGridItem(
                                modifier = Modifier.animateItem(),
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            Text(
                text = stringResource(R.string.no_articles_found),
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.no_articles_found_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.placeholder
            )
        }
    )
}

@Preview
@Composable
private fun NewsFeedScreenPreview() = AppTheme {
    NewsFeedScreenContent(
        query = "",
        state = NewsFeedState(
            isLoading = false,
            isRefreshing = false,
            displayStyle = DisplayStyle.List,
            articles = (1..10).map {
                Article(
                    id = "$it",
                    title = "Sample News Article $it",
                    description = "This is a sample description for news article $it.",
                    sourceName = "News Source $it",
                    publishedAt = ZonedDateTime.now(),
                    imageUrl = null,
                    sourceUrl = "https://example.com/article/$it",
                    content = null,
                    author = null
                )
            }.toImmutableList()
        ),
        onArticleClick = {},
        onEvent = {}
    )
}