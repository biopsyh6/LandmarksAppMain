package com.pavlusha.data.mapper

import com.pavlusha.data.local.entity.ExternalInfoEntity
import com.pavlusha.data.local.entity.HistoricalPeriodEntity
import com.pavlusha.data.local.entity.LandmarkCategoryEntity
import com.pavlusha.data.local.entity.LandmarkEntity
import com.pavlusha.data.local.entity.LandmarkGalleryEntity
import com.pavlusha.data.local.entity.LandmarkSourceUrlEntity
import com.pavlusha.data.local.entity.LandmarkTagEntity
import com.pavlusha.data.local.entity.relations.LandmarkWithDetails
import com.pavlusha.domain.model.CategoryType
import com.pavlusha.domain.model.ExternalInfoDomainModel
import com.pavlusha.domain.model.HistoricalPeriodDomainModel
import com.pavlusha.domain.model.LandmarkCategoryDomainModel
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource

object LandmarkLocalDataMapper {
    fun toDomainFromData(relation: LandmarkWithDetails): LandmarkDomainModel {
        val entity = relation.landmark

        return LandmarkDomainModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            shortDescription = entity.shortDescription,
            latitude = entity.latitude,
            longitude = entity.longitude,
            accuracyRadius = entity.accuracyRadius,

            category = LandmarkCategoryDomainModel(
                id = entity.category.categoryId,
                name = entity.category.categoryName,
                iconName = entity.category.iconName,
                colorHex = entity.category.colorHex,
                type = CategoryType.valueOf(entity.category.type),
                rawTypeName = entity.category.rawTypeName
            ),

            currentModel3dPath = entity.currentModel3dPath,
            mainImageUrl = entity.mainImageUrl,
            thumbnailUrl = entity.thumbnailUrl,

            historicalPeriods = relation.periods.map { it.toDomain() },
            galleryUrls = relation.gallery.map { it.imageUrl },
            tags = relation.tags.map { it.tag },
            sourceUrls = relation.sourceUrls.map { it.url },

            isFavorite = entity.isFavorite,
            isAvailableOffline = entity.isAvailableOffline,
            isPromoted = entity.isPromoted,
            source = LandmarkSource.valueOf(entity.source),

            distanceMeters = null,
            externalInfo = relation.externalInfo.map { it.toDomain() }
        )
    }

    fun fromDomainToData(domain: LandmarkDomainModel): LandmarkWithDetails {
        val landmarkId = domain.id

        val entity = LandmarkEntity(
            id = landmarkId,
            name = domain.name,
            description = domain.description,
            shortDescription = domain.shortDescription,
            latitude = domain.latitude,
            longitude = domain.longitude,
            accuracyRadius = domain.accuracyRadius,
            category = LandmarkCategoryEntity(
                categoryId = domain.category.id,
                categoryName = domain.category.name,
                iconName = domain.category.iconName,
                colorHex = domain.category.colorHex,
                type = domain.category.type.name,
                rawTypeName = domain.category.rawTypeName
            ),
            currentModel3dPath = domain.currentModel3dPath,
            remoteModel3dPath = domain.remoteModel3dPath,
            localModel3dPath = domain.localModel3dPath,
            mainImageUrl = domain.mainImageUrl,
            remoteMainImageUrl = domain.remoteMainImageUrl,
            localMainImagePath = domain.localMainImagePath,
            thumbnailUrl = domain.thumbnailUrl,
            isFavorite = domain.isFavorite,
            isAvailableOffline = domain.isAvailableOffline,
            isPromoted = domain.isPromoted,
            source = domain.source.name
        )

        return LandmarkWithDetails(
            landmark = entity,
            periods = domain.historicalPeriods.map { it.fromDomain(landmarkId) },
            gallery = domain.galleryUrls.map { LandmarkGalleryEntity(landmarkId = landmarkId, imageUrl = it) },
            tags = domain.tags.map { LandmarkTagEntity(landmarkId = landmarkId, tag = it) },
            sourceUrls = domain.sourceUrls.map { LandmarkSourceUrlEntity(landmarkId = landmarkId, url = it) },
            externalInfo = domain.externalInfo.map { it.fromDomain(landmarkId) }
        )
    }

    fun HistoricalPeriodEntity.toDomain() = HistoricalPeriodDomainModel(
        id = periodId,
        name = name,
        yearFrom = yearFrom,
        yearTo = yearTo,
        description = description,
        model3dPath = model3dPath
    )

    fun HistoricalPeriodDomainModel.fromDomain(landmarkId: String) = HistoricalPeriodEntity(
        periodId = id,
        landmarkId = landmarkId,
        name = name,
        yearFrom = yearFrom,
        yearTo = yearTo,
        description = description,
        model3dPath = model3dPath
    )

    fun ExternalInfoEntity.toDomain() = ExternalInfoDomainModel(
        sourceName = sourceName,
        webUrl = webUrl,
        summary = summary,
        rating = rating
    )

    fun ExternalInfoDomainModel.fromDomain(landmarkId: String) = ExternalInfoEntity(
        landmarkId = landmarkId,
        sourceName = sourceName,
        webUrl = webUrl,
        summary = summary,
        rating = rating
    )


}