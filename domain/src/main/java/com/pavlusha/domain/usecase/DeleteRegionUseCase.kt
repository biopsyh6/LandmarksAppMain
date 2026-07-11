package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IRegionRepository

class DeleteRegionUseCase(
    private val repository: IRegionRepository
) {
    suspend operator fun invoke(
        regionId: String
    ): TResult<Unit, AppExceptionDomainModel> {
        if (regionId.isBlank()) {
            return TResult.Error(
                AppExceptionDomainModel.Other(Exception("Region ID cannot be empty"))
            )
        }

        return repository.deleteRegion(regionId)
    }
}