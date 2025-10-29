package dev.logickoder.newshub.app

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import dev.logickoder.newshub.app.components.LocalToastManager
import dev.logickoder.newshub.app.components.ToastContainer
import dev.logickoder.newshub.app.components.ToastManager
import dev.logickoder.newshub.app.theme.AppTheme
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Composable
fun App(
    modifier: Modifier = Modifier,
) {
    val toastManager = remember { ToastManager() }
    val backStack = remember { mutableStateListOf<AppRoute>(AppRoute.Feed) }

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
                                }
                                entry<AppRoute.Details> { key ->
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