package com.pavlusha.domain.model

data class RecognitionResultDomainModel(
    val objectClassId: String,
    val landmarkId: String?,
    val className: String,
    val confidence: Float,
)
