package com.pavlusha.data.mapper

import com.pavlusha.data.local.entity.UserNoteEntity
import com.pavlusha.data.local.entity.VisitHistoryEntity
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.VisitHistoryDomainModel

object UserContentLocalDataMapper {
    fun toDomainFromData(entity: UserNoteEntity): UserNoteDomainModel {
        return UserNoteDomainModel(
            id = entity.id,
            landmarkId = entity.landmarkId,
            text = entity.text,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun fromDomainToData(domain: UserNoteDomainModel): UserNoteEntity {
        return UserNoteEntity(
            id = domain.id,
            landmarkId = domain.landmarkId,
            text = domain.text,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomainFromData(entity: VisitHistoryEntity): VisitHistoryDomainModel {
        return VisitHistoryDomainModel(
            id = entity.id,
            landmarkId = entity.landmarkId,
            visitDate = entity.visitDate,
            durationSeconds = entity.durationSeconds
        )
    }

    fun fromDomainToData(domain: VisitHistoryDomainModel): VisitHistoryEntity {
        return VisitHistoryEntity(
            id = domain.id,
            landmarkId = domain.landmarkId,
            visitDate = domain.visitDate,
            durationSeconds = domain.durationSeconds
        )
    }
}