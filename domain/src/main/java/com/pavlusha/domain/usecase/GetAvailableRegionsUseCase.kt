package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository

class GetAvailableRegionsUseCase(
    private val repository: ILandmarkRepository
) {
    suspend operator fun invoke(): TResult<List<RegionPackageDomainModel>, AppExceptionDomainModel> =
        repository.getAvailableRegions()
}