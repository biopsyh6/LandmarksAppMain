package com.pavlusha.domain.usecase

import com.pavlusha.domain.model.UserLocationDomainModel
import com.pavlusha.domain.repository.ILocationRepository

class GetUserLocationUseCase(
    private val repository: ILocationRepository
) {
    suspend operator fun invoke(): UserLocationDomainModel? {
        return repository.getCurrentLocation()
    }
}