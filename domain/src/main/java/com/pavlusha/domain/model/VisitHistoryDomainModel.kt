package com.pavlusha.domain.model

data class VisitHistoryDomainModel(
    val id: String,
    val landmarkId: String,
    val visitDate: Long,
    val durationSeconds: Int
)
