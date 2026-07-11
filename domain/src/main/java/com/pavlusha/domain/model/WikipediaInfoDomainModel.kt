package com.pavlusha.domain.model

data class WikipediaInfoDomainModel(
    val title: String,
    val extract: String,
    val thumbnailUrl: String?,
    val originalImageUrl: String?,
    val mobileArticleUrl: String?
)
