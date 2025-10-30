package dev.logickoder.newshub.app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.logickoder.newshub.app.theme.LocalAppColors
import kotlinx.coroutines.delay

enum class ToastType {
    Success,
    Error,
    Info,
    Warning
}

data class ToastData(
    val message: String,
    val duration: Long,
    val type: ToastType
)

class ToastManager {
    var currentToast by mutableStateOf<ToastData?>(null)
        private set

    fun show(message: String, duration: Long = 3000, type: ToastType = ToastType.Info) {
        currentToast = ToastData(message, duration, type)
    }

    fun dismiss() {
        currentToast = null
    }
}

@Composable
fun Toast(
    data: ToastData,
    onDismiss: () -> Unit
) {
    val colors = LocalAppColors.current

    // Determine colors and icon based on toast type
    val (backgroundColor, icon) = when (data.type) {
        ToastType.Success -> Pair(
            colors.success,
            Icons.Default.CheckCircle
        )

        ToastType.Error -> Pair(
            colors.error,
            Icons.Default.Error
        )

        ToastType.Info -> Pair(
            colors.primary,
            Icons.Default.Info
        )

        ToastType.Warning -> Pair(
            colors.secondary,
            Icons.Default.Warning
        )
    }

    LaunchedEffect(data) {
        delay(data.duration)
        onDismiss()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor),
        content = {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                content = {
                    Icon(
                        imageVector = icon,
                        contentDescription = data.type.name,
                        tint = colors.white
                    )
                    Text(
                        text = data.message,
                        color = colors.white,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }
    )
}

@Composable
fun ToastContainer(
    toastManager: ToastManager = LocalToastManager.current
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
        content = {
            AnimatedVisibility(
                visible = toastManager.currentToast != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                content = {
                    toastManager.currentToast?.let { toast ->
                        Toast(
                            data = toast,
                            onDismiss = { toastManager.dismiss() }
                        )
                    }
                }
            )
        }
    )
}

val LocalToastManager = staticCompositionLocalOf { ToastManager() }