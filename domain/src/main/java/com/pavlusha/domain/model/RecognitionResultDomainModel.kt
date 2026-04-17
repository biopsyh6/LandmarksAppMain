package com.pavlusha.domain.model

data class RecognitionResultDomainModel(
    val objectClassId: String,
    val className: String,
    val confidence: Float,
)
