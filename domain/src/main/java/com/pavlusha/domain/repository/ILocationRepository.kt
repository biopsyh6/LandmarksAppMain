package com.pavlusha.domain.repository

import com.pavlusha.domain.model.UserLocationDomainModel
import kotlinx.coroutines.flow.Flow

interface ILocationRepository {
    fun observeUserLocation(): Flow<UserLocationDomainModel>

    suspend fun getCurrentLocation(): UserLocationDomainModel?
}