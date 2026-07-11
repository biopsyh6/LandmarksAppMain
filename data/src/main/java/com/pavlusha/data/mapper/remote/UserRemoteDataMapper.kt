package com.pavlusha.data.mapper.remote

import com.pavlusha.data.model.remote.UserRemoteModel
import com.pavlusha.domain.model.UserDomainModel

object UserRemoteDataMapper {
    fun toDomainFromData(remote: UserRemoteModel): UserDomainModel {
        return UserDomainModel(
            id = remote.id,
            email = remote.email,
            displayName = remote.displayName,
            photoUrl = remote.photoUrl,
            isAnonymous = remote.isAnonymous
        )
    }

    fun fromDomainToData(domain: UserDomainModel): UserRemoteModel {
        return UserRemoteModel(
            id = domain.id,
            email = domain.email,
            displayName = domain.displayName,
            photoUrl = domain.photoUrl,
            isAnonymous = domain.isAnonymous
        )
    }
}