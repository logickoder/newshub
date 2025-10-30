package dev.logickoder.newshub.feed.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.logickoder.newshub.R
import dev.logickoder.newshub.app.domain.model.Article
import dev.logickoder.newshub.app.theme.LocalAppColors
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ArticleListItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    ItemCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        content = {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    ArticleImage(
                        imageUrl = article.imageUrl,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        content = {
                            Text(
                                text = article.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = colors.onSurface,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            article.description.takeIf { it.isNotBlank() }?.let { description ->
                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.placeholder,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                content = {
                                    Text(
                                        modifier = Modifier.weight(1f, fill = false),
                                        text = article.sourceName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.primary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.placeholder
                                    )
                                    Text(
                                        text = formatTimeAgo(article.publishedAt),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colors.placeholder
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
fun ArticleListItemShimmer(modifier: Modifier) {
    ItemCard(
        modifier = modifier.fillMaxWidth(),
        content = {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    ShimmerBox(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        content = {
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(16.dp)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(12.dp)
                            )
                            ShimmerBox(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                content = {
                                    ShimmerBox(
                                        modifier = Modifier
                                            .width(80.dp)
                                            .height(12.dp)
                                    )
                                    ShimmerBox(
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(12.dp)
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
fun ArticleGridItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    ItemCard(
        modifier = modifier.clickable(onClick = onClick),
        content = {
            ArticleImage(
                imageUrl = article.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                content = {
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = colors.onSurface,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    article.description.takeIf { it.isNotBlank() }?.let { description ->
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.placeholder,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        content = {
                            Text(
                                text = article.sourceName,
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = formatTimeAgo(article.publishedAt),
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.placeholder
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun ArticleGridItemShimmer(modifier: Modifier) {
    ItemCard(
        modifier = modifier,
        content = {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp)
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(16.dp)
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(16.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                    )
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ShimmerBox(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    ShimmerBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(12.dp)
                    )
                }
            )
        }
    )
}

@Composable
private fun ItemCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = LocalAppColors.current.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        content = content,
    )
}

@Composable
private fun ArticleImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current

    Box(
        modifier = modifier.background(
            color = colors.background,
            shape = RoundedCornerShape(8.dp)
        ),
        contentAlignment = Alignment.Center,
        content = {
            when {
                !imageUrl.isNullOrBlank() -> AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Article image",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )

                else -> Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = stringResource(R.string.no_image),
                    tint = colors.placeholder,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
private fun ShimmerBox(
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    val shimmerColor = colors.placeholder.copy(alpha = alpha)

    Box(
        modifier = modifier.background(
            color = shimmerColor,
            shape = RoundedCornerShape(4.dp)
        )
    )
}


private fun formatTimeAgo(publishedAt: ZonedDateTime): String {
    return try {
        publishedAt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault()))
    } catch (_: Exception) {
        "Recently"
    }
}
