package com.pavlusha.domain.model

data class LandmarkCategoryDomainModel(
    val id: String,
    val name: String,
    val iconName: String,
    val colorHex: String,
    val type: CategoryType,
    val rawTypeName: String,
)

enum class CategoryType {
    MONUMENT,
    ARCHITECTURE,
    RUINS,
    RELIGIOUS,
    MUSEUM,
    NATURAL_LANDMARK,
    HISTORIC_SITE,
    OTHER
}
