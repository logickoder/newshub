package dev.logickoder.newshub.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.logickoder.newshub.R
import dev.logickoder.newshub.app.components.LocalToastManager
import dev.logickoder.newshub.app.components.ToastType
import dev.logickoder.newshub.app.domain.model.Article
import dev.logickoder.newshub.app.theme.AppTheme
import dev.logickoder.newshub.app.theme.LocalAppColors
import dev.logickoder.newshub.details.domain.NewsDetailActions
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    article: Article,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val toastManager = LocalToastManager.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                color = colors.surface.copy(alpha = 0.9f),
                                shape = CircleShape
                            ),
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = colors.onSurface
                            )
                        }
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            NewsDetailActions.shareArticle(context, article)?.let {
                                toastManager.show(it, type = ToastType.Error)
                            }
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .background(
                                color = colors.surface.copy(alpha = 0.9f),
                                shape = CircleShape
                            ),
                        content = {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.share),
                                tint = colors.onSurface
                            )
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = colors.background,
        content = { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        content = {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(article.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.article_image),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.ic_placeholder),
                                error = painterResource(R.drawable.ic_placeholder),
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.3f)
                                            ),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                            )
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        content = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = article.title,
                                style = MaterialTheme.typography.headlineMedium.run {
                                    copy(
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = lineHeight * 1.2
                                    )
                                },
                                color = colors.onBackground,
                            )

                            ArticleMetadata(
                                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
                                article = article,
                            )

                            if (article.description.isNotBlank()) {
                                ArticleCard(
                                    modifier = Modifier
                                        .padding(bottom = 20.dp)
                                        .fillMaxWidth(),
                                    title = "Summary",
                                    description = article.description,
                                )
                            }

                            if (!article.content.isNullOrBlank() && article.content != article.description) {
                                val regex = remember { Regex("… \\[\\+\\d+ chars]$") }
                                val isTruncated = remember(article.content) {
                                    regex.containsMatchIn(article.content)
                                }
                                ArticleCard(
                                    modifier = Modifier
                                        .padding(bottom = 24.dp)
                                        .fillMaxWidth(),
                                    title = stringResource(R.string.article_content),
                                    description = remember(article.content) {
                                        // Remove … [+xx chars] pattern at end
                                        article.content.replace(regex, "")
                                            .trim() + (if (isTruncated) "…" else "")
                                    },
                                    content = {
                                        if (isTruncated) {
                                            Text(
                                                modifier = Modifier.padding(top = 8.dp),
                                                text = stringResource(R.string.article_truncated_info),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = colors.placeholder,
                                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                            )
                                        }
                                    }
                                )
                            }

                            Button(
                                modifier = Modifier
                                    .padding(bottom = 24.dp)
                                    .fillMaxWidth()
                                    .height(52.dp),
                                onClick = {
                                    NewsDetailActions.openCustomTab(context, article.sourceUrl)
                                        ?.let {
                                            toastManager.show(it, type = ToastType.Error)
                                        }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colors.primary,
                                    contentColor = colors.white
                                ),
                                shape = RoundedCornerShape(12.dp),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                ),
                                content = {
                                    Text(
                                        text = stringResource(R.string.read_full_article),
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
private fun ArticleMetadata(
    article: Article,
    modifier: Modifier = Modifier,
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current

    val formattedDate = remember(article.publishedAt) {
        try {
            article.publishedAt.format(
                DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
            )
        } catch (_: Exception) {
            context.getString(R.string.unknown_date)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_placeholder),
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = article.sourceName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = colors.primary
                    )
                }
            )

            if (!article.author.isNullOrBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    content = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = colors.placeholder,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = stringResource(R.string.by_x, article.author),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.onBackground
                        )
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = colors.placeholder,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.placeholder
                    )
                }
            )
        }
    )
}

@Composable
private fun ArticleCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    val colors = LocalAppColors.current
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = colors.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyLarge.run {
                            copy(lineHeight = lineHeight * 1.4)
                        },
                        color = colors.onSurface
                    )

                    if (content != null) {
                        content()
                    }
                }
            )
        }
    )
}

@Preview
@Composable
private fun NewsDetailScreenPreview() = AppTheme {
    NewsDetailScreen(
        article = Article(
            title = "Sample News Article Title That Is Quite Long to Test Text Wrapping in the UI",
            description = "This is a sample description of the news article. It provides a brief summary of the content.",
            content = "This is the full content of the news article. It goes into more detail about the topic discussed in the article. The content may include various paragraphs, quotes, and other relevant information that provides a comprehensive understanding of the subject matter… [+2527 chars]",
            author = "John Doe",
            sourceName = "News Source",
            sourceUrl = "https://www.newssource.com/sample-article",
            imageUrl = "",
            publishedAt = ZonedDateTime.now(),
            id = "1"
        ),
        onBack = {}
    )
}