package com.pavlusha.data.model.remote

data class UserNoteRemoteModel(
    val id: String = "",
    val userId: String = "",
    val landmarkId: String = "",
    val text: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
