package dev.logickoder.newshub.app

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.logickoder.newshub.app.components.LocalToastManager
import dev.logickoder.newshub.app.components.ToastContainer
import dev.logickoder.newshub.app.components.ToastManager
import dev.logickoder.newshub.app.components.globalToastManager
import dev.logickoder.newshub.app.theme.AppTheme
import dev.logickoder.newshub.feed.NewsFeedScreen
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Composable
fun App(
    modifier: Modifier = Modifier,
) {
    val toastManager = remember { ToastManager() }
    val backStack = remember { mutableStateListOf<AppRoute>(AppRoute.Feed) }

    DisposableEffect(Unit) {
        globalToastManager = toastManager

        onDispose {
            globalToastManager = null
        }
    }

    AppTheme {
        CompositionLocalProvider(
            LocalToastManager provides toastManager,
            content = {
                Box(
                    modifier = modifier,
                    content = {
                        NavDisplay(
                            modifier = modifier,
                            backStack = backStack,
                            entryProvider = entryProvider {
                                entry<AppRoute.Feed> {
                                    NewsFeedScreen(
                                        onArticleClick = { article ->
                                            backStack.add(AppRoute.Details(article.id))
                                        }
                                    )
                                }
                                entry<AppRoute.Details> { key ->
                                    // TODO: Implement ArticleDetailScreen
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Article Details: ${key.id}")
                                    }
                                }
                            }
                        )
                        ToastContainer()
                    }
                )
            }
        )
    }
}

@Serializable
private sealed interface AppRoute : Parcelable {
    @Serializable
    @Parcelize
    data object Feed : AppRoute

    @Serializable
    @Parcelize
    data class Details(val id: String) : AppRoute
}