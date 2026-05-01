package com.pavlusha.data.mapper

import com.pavlusha.domain.model.CategoryType
import com.pavlusha.domain.model.ExternalInfoDomainModel
import com.pavlusha.domain.model.LandmarkCategoryDomainModel
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.SearchResultDomainModel
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.search.BusinessObjectMetadata
import com.yandex.mapkit.uri.UriObjectMetadata

object SearchDataMapper {
    fun fromYandexToSearchResultDomain(geoObject: GeoObject): SearchResultDomainModel {
        val businessMetadata = geoObject.metadataContainer.getItem(BusinessObjectMetadata::class.java)
        val uriMetadata = geoObject.metadataContainer.getItem(UriObjectMetadata::class.java)
        val descriptionFromCategories = businessMetadata?.categories?.joinToString { it.name }

        return SearchResultDomainModel(
            id = uriMetadata?.uris?.firstOrNull()?.value ?: geoObject.name ?: "",
            name = geoObject.name ?: "Неизвестное место",
            address = geoObject.descriptionText,
            descriptionFromCategories = descriptionFromCategories,
            latitude = geoObject.geometry.firstOrNull()?.point?.latitude ?: 0.0,
            longitude = geoObject.geometry.firstOrNull()?.point?.longitude ?: 0.0,
            categoryName = businessMetadata?.categories?.firstOrNull()?.name ?: "Достопримечательность",
            thumbnailUrl = null,
            distanceMeters = null,
            source = LandmarkSource.REMOTE_API,
            isPromoted = false
        )
    }

    fun fromYandexToLandmarkDomain(geoObject: GeoObject): LandmarkDomainModel {
        val businessMetadata = geoObject.metadataContainer.getItem(BusinessObjectMetadata::class.java)
        val point = geoObject.geometry.firstOrNull()?.point

        return LandmarkDomainModel(
            id = geoObject.metadataContainer.getItem(UriObjectMetadata::class.java)?.uris?.firstOrNull()?.value ?: "",
            name = geoObject.name ?: "",
            description = geoObject.descriptionText ?: "",
            shortDescription = businessMetadata?.categories?.firstOrNull()?.name ?: "",
            latitude = point?.latitude ?: 0.0,
            longitude = point?.longitude ?: 0.0,
            category = LandmarkCategoryDomainModel(
                id = "external",
                name = businessMetadata?.categories?.firstOrNull()?.name ?: "Другое",
                iconName = "ic_place",
                colorHex = "#FF0000",
                type = CategoryType.OTHER,
                rawTypeName = businessMetadata?.categories?.firstOrNull()?.name ?: "other"
            ),
            source = LandmarkSource.REMOTE_API,
            isPromoted = false,
            externalInfo = listOfNotNull(
                businessMetadata?.links?.firstOrNull()?.let { link ->
                    ExternalInfoDomainModel(
                        sourceName = "Yandex Maps",
                        webUrl = link.link.href,
                        summary = "Открыть в Яндекс Картах",
                    )
                }
            )
        )
    }
}