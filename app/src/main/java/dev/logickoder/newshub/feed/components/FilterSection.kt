package dev.logickoder.newshub.feed.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.logickoder.newshub.R
import dev.logickoder.newshub.app.domain.model.ArticleCategory
import dev.logickoder.newshub.app.domain.model.ArticleSortBy
import dev.logickoder.newshub.app.domain.model.ArticleType
import dev.logickoder.newshub.app.theme.AppTheme
import dev.logickoder.newshub.app.theme.LocalAppColors
import dev.logickoder.newshub.feed.NewsFeedViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterSection(
    articleType: ArticleType,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onArticleTypeChanged: (ArticleType) -> Unit,
    onCategoryChanged: (ArticleCategory) -> Unit,
    onFromDateChanged: (LocalDate?) -> Unit,
    onToDateChanged: (LocalDate?) -> Unit,
    onSortByChanged: (ArticleSortBy?) -> Unit,
    onDomainsChanged: (ImmutableList<String>) -> Unit,
) {
    val colors = LocalAppColors.current

    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        content = {
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                shape = RoundedCornerShape(12.dp),
                content = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        content = {
                            Section {
                                Title(R.string.article_type)

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    content = {
                                        Chip(
                                            selected = articleType is ArticleType.Headline,
                                            onClick = {
                                                onArticleTypeChanged(ArticleType.Headline())
                                            },
                                            label = stringResource(R.string.headlines),
                                        )

                                        Chip(
                                            selected = articleType is ArticleType.Everything,
                                            onClick = {
                                                onArticleTypeChanged(ArticleType.Everything())
                                            },
                                            label = stringResource(R.string.everything),
                                        )
                                    }
                                )
                            }

                            when (articleType) {
                                is ArticleType.Headline -> HeadlineFilters(
                                    selectedCategory = articleType.category,
                                    onCategoryChanged = onCategoryChanged
                                )

                                is ArticleType.Everything -> EverythingFilters(
                                    fromDate = articleType.from,
                                    toDate = articleType.to,
                                    selectedSortBy = articleType.sortBy,
                                    selectedDomains = articleType.domains,
                                    onFromDateChanged = onFromDateChanged,
                                    onToDateChanged = onToDateChanged,
                                    onSortByChanged = onSortByChanged,
                                    onDomainsChanged = onDomainsChanged,
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeadlineFilters(
    selectedCategory: ArticleCategory,
    onCategoryChanged: (ArticleCategory) -> Unit
) {
    Section {
        Title(R.string.category)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                ArticleCategory.entries.forEach { category ->
                    Chip(
                        selected = selectedCategory == category,
                        onClick = { onCategoryChanged(category) },
                        label = category.name,
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun EverythingFilters(
    fromDate: LocalDate?,
    toDate: LocalDate?,
    selectedSortBy: ArticleSortBy?,
    selectedDomains: ImmutableList<String>,
    onFromDateChanged: (LocalDate?) -> Unit,
    onToDateChanged: (LocalDate?) -> Unit,
    onSortByChanged: (ArticleSortBy?) -> Unit,
    onDomainsChanged: (ImmutableList<String>) -> Unit,
) {
    val colors = LocalAppColors.current

    Section {
        Title(R.string.date_range)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = {
                DatePickerField(
                    modifier = Modifier.weight(1f),
                    label = R.string.from,
                    selectedDate = fromDate,
                    onDateSelected = onFromDateChanged
                )

                Text(
                    text = "-",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.placeholder
                )

                DatePickerField(
                    modifier = Modifier.weight(1f),
                    label = R.string.to,
                    selectedDate = toDate,
                    onDateSelected = onToDateChanged
                )
            }
        )
    }

    Section {
        Title(R.string.sort_by)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                Chip(
                    selected = selectedSortBy == null,
                    onClick = { onSortByChanged(null) },
                    label = stringResource(R.string._default),
                )

                ArticleSortBy.entries.forEach { sortBy ->
                    Chip(
                        selected = selectedSortBy == sortBy,
                        onClick = { onSortByChanged(sortBy) },
                        label = sortBy.name,
                    )
                }
            }
        )
    }

    Section {
        Title(R.string.sources)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                NewsFeedViewModel.availableDomains.forEach { domain ->
                    Chip(
                        selected = selectedDomains.contains(domain),
                        onClick = {
                            onDomainsChanged(
                                when {
                                    selectedDomains.contains(domain) && selectedDomains.size > 1 -> {
                                        selectedDomains - domain
                                    }
                                    // Don't allow removing if it's the last domain
                                    selectedDomains.contains(domain) -> selectedDomains
                                    else -> selectedDomains + domain
                                }.toImmutableList()
                            )
                        },
                        label = domain,
                    )
                }
            }
        )
    }
}

@Composable
private fun Section(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@Composable
private fun Title(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(title),
        style = MaterialTheme.typography.titleSmall,
        color = LocalAppColors.current.onSurface,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun Chip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = LocalAppColors.current.primary.copy(alpha = 0.2f),
            selectedLabelColor = LocalAppColors.current.primary
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    @StringRes label: Int,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = selectedDate?.format(DateTimeFormatter.ISO_LOCAL_DATE).orEmpty(),
        onValueChange = { },
        label = {
            Text(stringResource(label))
        },
        placeholder = { Text(stringResource(R.string.date_placeholder), maxLines = 1) },
        readOnly = true,
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = { showDatePicker = true },
                content = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = stringResource(R.string.select_date)
                    )
                }
            )
        },
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneId.systemDefault())
                ?.toInstant()?.toEpochMilli()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(localDate)
                        }
                        showDatePicker = false
                    },
                    content = {
                        Text(stringResource(R.string.ok))
                    }
                )
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    content = {
                        Text(stringResource(R.string.cancel))
                    }
                )
            },
            content = {
                DatePicker(state = datePickerState)
            }
        )
    }
}

@Preview
@Composable
private fun FilterSectionPreview() = AppTheme {
    FilterSection(
        articleType = ArticleType.Everything(),
        isVisible = true,
        onArticleTypeChanged = {},
        onCategoryChanged = {},
        onFromDateChanged = {},
        onToDateChanged = {},
        onSortByChanged = {},
        onDomainsChanged = {}
    )
}
