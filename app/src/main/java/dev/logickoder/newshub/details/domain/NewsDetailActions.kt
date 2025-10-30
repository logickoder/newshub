package dev.logickoder.newshub.details.domain

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import dev.logickoder.newshub.R
import dev.logickoder.newshub.app.domain.model.Article
import io.github.aakira.napier.Napier

object NewsDetailActions {
    fun openCustomTab(context: Context, url: String): String? = try {
        val intent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setUrlBarHidingEnabled(false)
            .build()

        intent.launchUrl(context, url.toUri())
        null
    } catch (_: Exception) {
        try {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                url.toUri()
            )
            context.startActivity(browserIntent)
            null
        } catch (ex: Exception) {
            Napier.e("Cannot open URL: $url", ex)
            ex.localizedMessage.takeIf { !it.isNullOrBlank() }
                ?: context.getString(R.string.custom_tab_error)
        }
    }

    fun shareArticle(context: Context, article: Article): String? {
        val shareText = "${article.title}\n\n${article.sourceUrl}"
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, article.title)
        }

        return try {
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.share_article)
                )
            )
            null
        } catch (e: Exception) {
            Napier.e("Cannot share article", e)
            e.localizedMessage.takeIf { !it.isNullOrBlank() }
                ?: context.getString(R.string.failed_to_share_article)
        }
    }
}