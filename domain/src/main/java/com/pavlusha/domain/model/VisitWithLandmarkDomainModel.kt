package com.pavlusha.domain.model

data class VisitWithLandmarkDomainModel(
    val visit: VisitHistoryDomainModel,
    val landmark: LandmarkDomainModel
)
