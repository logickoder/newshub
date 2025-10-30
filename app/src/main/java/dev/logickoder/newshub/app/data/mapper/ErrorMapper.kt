package dev.logickoder.newshub.app.data.mapper

import dev.logickoder.newshub.app.data.remote.dto.ErrorDto
import dev.logickoder.newshub.app.domain.AppJson
import retrofit2.HttpException
import java.io.IOException

object ErrorMapper {
    operator fun invoke(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> throwable.response()?.errorBody()?.string()?.let {
                AppJson.decodeFromString<ErrorDto>(it)
            }?.message

            is IOException -> "Please check your internet connection and try again."

            else -> null
        } ?: throwable.localizedMessage.orEmpty()
    }
}