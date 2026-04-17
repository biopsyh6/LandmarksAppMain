package com.pavlusha.domain.model

data class HistoricalPeriodDomainModel(
    val id: String,
    val name: String,
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val description: String? = null,
    val model3dPath: String? = null
)
