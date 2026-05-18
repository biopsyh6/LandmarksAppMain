package com.pavlusha.data.mapper.remote

import com.pavlusha.data.model.remote.UserNoteRemoteModel
import com.pavlusha.data.model.remote.VisitHistoryRemoteModel
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.VisitHistoryDomainModel

object UserContentRemoteDataMapper {
    fun toDomainFromData(remote: UserNoteRemoteModel): UserNoteDomainModel {
        return UserNoteDomainModel(
            id = remote.id,
            userId = remote.userId,
            landmarkId = remote.landmarkId,
            text = remote.text,
            createdAt = remote.createdAt,
            updatedAt = remote.updatedAt
        )
    }

    fun fromDomainToData(domain: UserNoteDomainModel): UserNoteRemoteModel {
        return UserNoteRemoteModel(
            id = domain.id,
            userId = domain.userId,
            landmarkId = domain.landmarkId,
            text = domain.text,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomainFromData(remote: VisitHistoryRemoteModel): VisitHistoryDomainModel {
        return VisitHistoryDomainModel(
            id = remote.id,
            userId = remote.userId,
            landmarkId = remote.landmarkId,
            visitDate = remote.visitDate,
            durationSeconds = remote.durationSeconds
        )
    }

    fun fromDomainToData(domain: VisitHistoryDomainModel): VisitHistoryRemoteModel {
        return VisitHistoryRemoteModel(
            id = domain.id,
            userId = domain.userId,
            landmarkId = domain.landmarkId,
            visitDate = domain.visitDate,
            durationSeconds = domain.durationSeconds
        )
    }
}