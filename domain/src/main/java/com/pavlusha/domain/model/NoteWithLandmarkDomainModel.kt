package com.pavlusha.domain.model

data class NoteWithLandmarkDomainModel(
    val note: UserNoteDomainModel,
    val landmark: LandmarkDomainModel
)
