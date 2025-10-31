package dev.logickoder.newshub.app.data.remote.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test

class ApiKeyInterceptorTest {

    private val mockChain = mockk<Interceptor.Chain>()
    private val mockResponse = mockk<Response>()
    private lateinit var interceptor: ApiKeyInterceptor

    @Before
    fun setup() {
        interceptor = ApiKeyInterceptor()
    }

    @Test
    fun `intercept adds API key to request URL`() {
        // Given
        val originalUrl = "https://newsapi.org/v2/top-headlines"
        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { mockChain.request() } returns originalRequest
        every { mockChain.proceed(any()) } returns mockResponse

        // When
        val result = interceptor.intercept(mockChain)

        // Then
        assertThat(result).isEqualTo(mockResponse)

        verify {
            mockChain.proceed(match { request ->
                request.url.queryParameter("apiKey") != null &&
                        request.url.queryParameter("apiKey")!!.isNotBlank()
            })
        }
    }

    @Test
    fun `intercept preserves existing query parameters`() {
        // Given
        val originalUrl = "https://newsapi.org/v2/top-headlines?country=us&category=technology"
        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { mockChain.request() } returns originalRequest
        every { mockChain.proceed(any()) } returns mockResponse

        // When
        interceptor.intercept(mockChain)

        // Then
        verify {
            mockChain.proceed(match { request ->
                request.url.queryParameter("country") == "us" &&
                        request.url.queryParameter("category") == "technology" &&
                        request.url.queryParameter("apiKey") != null
            })
        }
    }

    @Test
    fun `intercept preserves request method and headers`() {
        // Given
        val originalRequest = Request.Builder()
            .url("https://newsapi.org/v2/top-headlines")
            .addHeader("User-Agent", "NewsHub-Android")
            .addHeader("Accept", "application/json")
            .post(mockk())
            .build()

        every { mockChain.request() } returns originalRequest
        every { mockChain.proceed(any()) } returns mockResponse

        // When
        interceptor.intercept(mockChain)

        // Then
        verify {
            mockChain.proceed(match { request ->
                request.method == "POST" &&
                        request.header("User-Agent") == "NewsHub-Android" &&
                        request.header("Accept") == "application/json" &&
                        request.body != null
            })
        }
    }

    @Test
    fun `intercept works with different base URLs`() {
        // Given
        val originalUrl = "https://newsapi.org/v2/everything"
        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { mockChain.request() } returns originalRequest
        every { mockChain.proceed(any()) } returns mockResponse

        // When
        interceptor.intercept(mockChain)

        // Then
        verify {
            mockChain.proceed(match { request ->
                request.url.toString().startsWith("https://newsapi.org/v2/everything") &&
                        request.url.queryParameter("apiKey") != null
            })
        }
    }

    @Test
    fun `intercept handles URL with fragment`() {
        // Given
        val originalUrl = "https://newsapi.org/v2/top-headlines#section"
        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { mockChain.request() } returns originalRequest
        every { mockChain.proceed(any()) } returns mockResponse

        // When
        interceptor.intercept(mockChain)

        // Then
        verify {
            mockChain.proceed(match { request ->
                request.url.fragment == "section" &&
                        request.url.queryParameter("apiKey") != null
            })
        }
    }

    @Test
    fun `intercept maintains URL encoding`() {
        // Given
        val originalUrl = "https://newsapi.org/v2/everything?q=android%20development"
        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        every { mockChain.request() } returns originalRequest
        every { mockChain.proceed(any()) } returns mockResponse

        // When
        interceptor.intercept(mockChain)

        // Then
        verify {
            mockChain.proceed(match { request ->
                request.url.queryParameter("q") == "android development" &&
                        request.url.queryParameter("apiKey") != null
            })
        }
    }
}