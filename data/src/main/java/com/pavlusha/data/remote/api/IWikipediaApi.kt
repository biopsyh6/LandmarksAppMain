package com.pavlusha.data.remote.api

import com.pavlusha.data.model.remote.WikipediaSummaryRemoteModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IWikipediaApi {
    @GET("api/rest_v1/page/summary/{title}")
    suspend fun getArticleSummary(
        @Path("title") title: String,
        @Header("User-Agent") userAgent: String = "LandmarkARApp/1.0"
    ): Response<WikipediaSummaryRemoteModel>
}