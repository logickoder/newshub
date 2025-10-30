package dev.logickoder.newshub.app.data.remote

import dev.logickoder.newshub.app.data.remote.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getHeadlines(
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("q") query: String?,
        @Query("category") category: String?,
    ): NewsResponse

    @GET("everything")
    suspend fun getEverything(
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("q") query: String?,
        @Query("from") from: String?,
        @Query("to") to: String?,
        @Query("sortBy") sortBy: String?,
    ): NewsResponse
}