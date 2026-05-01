package com.pavlusha.data.mapper

import com.pavlusha.data.local.entity.UserEntity
import com.pavlusha.domain.model.UserDomainModel

object UserLocalDataMapper {
    fun toDomainFromData(entity: UserEntity): UserDomainModel {
        return UserDomainModel(
            id = entity.id,
            email = entity.email,
            displayName = entity.displayName,
            photoUrl = entity.photoUrl,
            isAnonymous = entity.isAnonymous
        )
    }

    fun fromDomainToData(domain: UserDomainModel): UserEntity {
        return UserEntity(
            id = domain.id,
            email = domain.email,
            displayName = domain.displayName,
            photoUrl = domain.photoUrl,
            isAnonymous = domain.isAnonymous
        )
    }
}