package dev.logickoder.newshub.app.data.mapper

import com.google.common.truth.Truth.assertThat
import dev.logickoder.newshub.app.data.remote.dto.ErrorDto
import dev.logickoder.newshub.app.domain.AppJson
import io.mockk.every
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorMapperTest {

    @Test
    fun `maps IOException to network error message`() {
        // Given
        val ioException = IOException("Network unreachable")

        // When
        val errorMessage = ErrorMapper(ioException)

        // Then
        assertThat(errorMessage).isEqualTo("Please check your internet connection and try again.")
    }

    @Test
    fun `maps UnknownHostException to network error message`() {
        // Given
        val hostException = UnknownHostException("Unable to resolve host")

        // When
        val errorMessage = ErrorMapper(hostException)

        // Then
        assertThat(errorMessage).isEqualTo("Please check your internet connection and try again.")
    }

    @Test
    fun `maps SocketTimeoutException to network error message`() {
        // Given
        val timeoutException = SocketTimeoutException("Read timeout")

        // When
        val errorMessage = ErrorMapper(timeoutException)

        // Then
        assertThat(errorMessage).isEqualTo("Please check your internet connection and try again.")
    }

    @Test
    fun `maps HttpException with valid error body to API error message`() {
        // Given
        val errorDto = ErrorDto(
            status = "error",
            code = "apiKeyInvalid",
            message = "Your API key is invalid or incorrect."
        )
        val jsonError = AppJson.encodeToString(ErrorDto.serializer(), errorDto)
        val errorBody = jsonError.toResponseBody("application/json".toMediaType())

        val mockResponse = mockk<Response<*>>()
        every { mockResponse.errorBody() } returns errorBody
        every { mockResponse.code() } returns 401
        every { mockResponse.message() } returns "Unauthorized"

        val httpException = HttpException(mockResponse)

        // When
        val errorMessage = ErrorMapper(httpException)

        // Then
        assertThat(errorMessage).isEqualTo("Your API key is invalid or incorrect.")
    }

    @Test
    fun `maps HttpException without error body to localized message`() {
        // Given
        val mockResponse = mockk<Response<*>>()
        every { mockResponse.errorBody() } returns null
        every { mockResponse.code() } returns 500
        every { mockResponse.message() } returns "Internal Server Error"

        val httpException = HttpException(mockResponse)

        // When  
        val errorMessage = ErrorMapper(httpException)

        // Then
        assertThat(errorMessage).contains("500")
    }

    @Test
    fun `maps HttpException with error JSON to localized message`() {
        // Given
        val errorJson = "{message: 'error_json', status: 'error', code: '400'}"
        val errorBody = errorJson.toResponseBody("application/json".toMediaType())

        val mockResponse = mockk<Response<*>>()
        every { mockResponse.errorBody() } returns errorBody
        every { mockResponse.code() } returns 400
        every { mockResponse.message() } returns "Bad Request"

        val httpException = HttpException(mockResponse)

        // When
        val errorMessage = ErrorMapper(httpException)

        // Then
        assertThat(errorMessage).contains("error_json")
    }

    @Test
    fun `maps generic exception to localized message`() {
        // Given
        val genericException = RuntimeException("Something went wrong")

        // When
        val errorMessage = ErrorMapper(genericException)

        // Then
        assertThat(errorMessage).isEqualTo("Something went wrong")
    }

    @Test
    fun `maps exception with null message to empty string`() {
        // Given
        val exceptionWithNullMessage = RuntimeException(null as String?)

        // When
        val errorMessage = ErrorMapper(exceptionWithNullMessage)

        // Then
        assertThat(errorMessage).isEmpty()
    }

    @Test
    fun `maps NullPointerException correctly`() {
        // Given
        val nullPointerException = NullPointerException("Null value encountered")

        // When
        val errorMessage = ErrorMapper(nullPointerException)

        // Then
        assertThat(errorMessage).isEqualTo("Null value encountered")
    }

    @Test
    fun `handles HttpException with empty error body`() {
        // Given
        val emptyErrorBody = "{}".toResponseBody("application/json".toMediaType())

        val mockResponse = mockk<Response<*>>()
        every { mockResponse.errorBody() } returns emptyErrorBody
        every { mockResponse.code() } returns 403
        every { mockResponse.message() } returns "Forbidden"

        val httpException = HttpException(mockResponse)

        // When
        val errorMessage = ErrorMapper(httpException)

        // Then
        assertThat(errorMessage).contains("403")
    }
}