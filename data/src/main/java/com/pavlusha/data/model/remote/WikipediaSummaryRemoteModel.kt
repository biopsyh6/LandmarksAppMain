package com.pavlusha.data.model.remote

import com.google.gson.annotations.SerializedName

data class WikipediaSummaryRemoteModel(
    @SerializedName("title")
    val title: String?,

    @SerializedName("extract")
    val extract: String?,

    @SerializedName("thumbnail")
    val thumbnail: WikiImageResponse?,

    @SerializedName("originalimage")
    val originalImage: WikiImageResponse?,

    @SerializedName("content_urls")
    val contentUrls: WikiContentUrlsResponse?
)

data class WikiImageResponse(
    @SerializedName("source") val source: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?
)

data class WikiContentUrlsResponse(
    @SerializedName("mobile") val mobile: WikiMobileUrlsResponse?
)

data class WikiMobileUrlsResponse(
    @SerializedName("page") val page: String?
)