package dev.logickoder.newshub.app.data.remote.interceptor

import dev.logickoder.newshub.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val key = BuildConfig.NEWS_API_KEY
        when {
            // No API key provided, fail the request
            key.isBlank() -> throw IllegalStateException("NEWS_API_KEY is not set in local.properties")
            else -> {
                val original = chain.request()

                val url = original.url.newBuilder()
                    .addQueryParameter("apiKey", key)
                    .build()

                val request = original.newBuilder()
                    .url(url)
                    .build()

                return chain.proceed(request)
            }
        }
    }
}