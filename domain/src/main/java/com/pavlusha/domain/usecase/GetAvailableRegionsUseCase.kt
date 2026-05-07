package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.IRegionRepository

class GetAvailableRegionsUseCase(
    private val repository: IRegionRepository
) {
    suspend operator fun invoke(): TResult<List<RegionPackageDomainModel>, AppExceptionDomainModel> =
        repository.getAvailableRegions()
}