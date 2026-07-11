package com.pavlusha.domain.usecase

import com.pavlusha.domain.model.UserLocationDomainModel
import com.pavlusha.domain.repository.ILocationRepository
import kotlinx.coroutines.flow.Flow

class ObserveUserLocationUseCase(
    private val repository: ILocationRepository
) {
    operator fun invoke(): Flow<UserLocationDomainModel> =
        repository.observeUserLocation()
}