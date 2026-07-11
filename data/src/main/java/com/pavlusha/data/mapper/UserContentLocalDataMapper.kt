package com.pavlusha.data.mapper

import com.pavlusha.data.local.entity.UserARPhotoEntity
import com.pavlusha.data.local.entity.UserNoteEntity
import com.pavlusha.data.local.entity.VisitHistoryEntity
import com.pavlusha.domain.model.UserARPhotoDomainModel
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.VisitHistoryDomainModel

object UserContentLocalDataMapper {
    fun toDomainFromData(entity: UserNoteEntity): UserNoteDomainModel {
        return UserNoteDomainModel(
            id = entity.id,
            userId = entity.userId,
            landmarkId = entity.landmarkId,
            text = entity.text,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun fromDomainToData(domain: UserNoteDomainModel): UserNoteEntity {
        return UserNoteEntity(
            id = domain.id,
            userId = domain.userId,
            landmarkId = domain.landmarkId,
            text = domain.text,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomainFromData(entity: VisitHistoryEntity): VisitHistoryDomainModel {
        return VisitHistoryDomainModel(
            id = entity.id,
            userId = entity.userId,
            landmarkId = entity.landmarkId,
            visitDate = entity.visitDate,
            durationSeconds = entity.durationSeconds
        )
    }

    fun fromDomainToData(domain: VisitHistoryDomainModel): VisitHistoryEntity {
        return VisitHistoryEntity(
            id = domain.id,
            userId = domain.userId,
            landmarkId = domain.landmarkId,
            visitDate = domain.visitDate,
            durationSeconds = domain.durationSeconds
        )
    }

    fun toDomainFromData(entity: UserARPhotoEntity): UserARPhotoDomainModel {
        return UserARPhotoDomainModel(
            id = entity.id,
            userId = entity.userId,
            landmarkId = entity.landmarkId,
            localFilePath = entity.localFilePath,
            remoteUrl = entity.remoteUrl,
            createdAt = entity.createdAt
        )
    }

    fun fromDomainToData(domain: UserARPhotoDomainModel): UserARPhotoEntity {
        return UserARPhotoEntity(
            id = domain.id,
            userId = domain.userId,
            landmarkId = domain.landmarkId,
            localFilePath = domain.localFilePath,
            remoteUrl = domain.remoteUrl,
            createdAt = domain.createdAt
        )
    }
}