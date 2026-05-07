package com.pavlusha.data.mapper.remote

import com.pavlusha.data.model.remote.ExternalInfoRemoteModel
import com.pavlusha.data.model.remote.HistoricalPeriodRemoteModel
import com.pavlusha.data.model.remote.LandmarkCategoryRemoteModel
import com.pavlusha.data.model.remote.LandmarkRemoteModel
import com.pavlusha.domain.model.CategoryType
import com.pavlusha.domain.model.ExternalInfoDomainModel
import com.pavlusha.domain.model.HistoricalPeriodDomainModel
import com.pavlusha.domain.model.LandmarkCategoryDomainModel
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource

object LandmarkRemoteDataMapper {
    fun toDomainFromData(remote: LandmarkRemoteModel): LandmarkDomainModel {
        return LandmarkDomainModel(
            id = remote.id,
            name = remote.name,
            description = remote.description,
            shortDescription = remote.shortDescription,
            latitude = remote.latitude,
            longitude = remote.longitude,
            accuracyRadius = remote.accuracyRadius,

            category = LandmarkCategoryDomainModel(
                id = remote.category.id,
                name = remote.category.name,
                iconName = remote.category.iconName,
                colorHex = remote.category.colorHex,
                type = runCatching { CategoryType.valueOf(remote.category.type) }.getOrDefault(
                    CategoryType.OTHER
                ),
                rawTypeName = remote.category.rawTypeName
            ),

            remoteModel3dPath = remote.remoteModel3dPath,
            remoteMainImageUrl = remote.remoteMainImageUrl,
            thumbnailUrl = remote.thumbnailUrl,

            currentModel3dPath = null,
            localModel3dPath = null,
            localMainImagePath = null,

            historicalPeriods = remote.historicalPeriods.map { it.toDomain() },
            galleryUrls = remote.galleryUrls,
            tags = remote.tags,
            sourceUrls = remote.sourceUrls,
            externalInfo = remote.externalInfo.map { it.toDomain() },

            isFavorite = false,
            isAvailableOffline = false,
            distanceMeters = null,

            isPromoted = remote.isPromoted,
            source = runCatching { LandmarkSource.valueOf(remote.source) }.getOrDefault(LandmarkSource.REMOTE_API)
        )
    }

    fun fromDomainToData(domain: LandmarkDomainModel): LandmarkRemoteModel {
        return LandmarkRemoteModel(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            shortDescription = domain.shortDescription,
            latitude = domain.latitude,
            longitude = domain.longitude,
            accuracyRadius = domain.accuracyRadius,
            category = LandmarkCategoryRemoteModel(
                id = domain.category.id,
                name = domain.category.name,
                iconName = domain.category.iconName,
                colorHex = domain.category.colorHex,
                type = domain.category.type.name,
                rawTypeName = domain.category.rawTypeName
            ),
            remoteModel3dPath = domain.remoteModel3dPath,
            remoteMainImageUrl = domain.remoteMainImageUrl,
            thumbnailUrl = domain.thumbnailUrl,
            galleryUrls = domain.galleryUrls,
            tags = domain.tags,
            sourceUrls = domain.sourceUrls,
            historicalPeriods = domain.historicalPeriods.map { it.fromDomain() },
            externalInfo = domain.externalInfo.map { it.fromDomain() },
            isPromoted = domain.isPromoted,
            source = domain.source.name
        )
    }

    private fun HistoricalPeriodRemoteModel.toDomain() = HistoricalPeriodDomainModel(
        id = id,
        name = name,
        yearFrom = yearFrom,
        yearTo = yearTo,
        description = description,
        model3dPath = model3dPath
    )

    private fun HistoricalPeriodDomainModel.fromDomain() = HistoricalPeriodRemoteModel(
        id = id,
        name = name,
        yearFrom = yearFrom,
        yearTo = yearTo,
        description = description,
        model3dPath = model3dPath
    )

    private fun ExternalInfoRemoteModel.toDomain() = ExternalInfoDomainModel(
        sourceName = sourceName,
        webUrl = webUrl,
        summary = summary,
        rating = rating
    )

    private fun ExternalInfoDomainModel.fromDomain() = ExternalInfoRemoteModel(
        sourceName = sourceName,
        webUrl = webUrl,
        summary = summary,
        rating = rating
    )
}