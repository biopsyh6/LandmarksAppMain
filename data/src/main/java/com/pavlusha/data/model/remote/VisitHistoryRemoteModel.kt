package com.pavlusha.data.model.remote

data class VisitHistoryRemoteModel(
    val id: String = "",
    val landmarkId: String = "",
    val visitDate: Long = 0L,
    val durationSeconds: Int = 0
)
