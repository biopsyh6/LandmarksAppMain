package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IRegionRepository
import kotlinx.coroutines.flow.Flow

class DownloadRegionUseCase(
    private val repository: IRegionRepository
) {
    operator fun invoke(regionId: String): Flow<TResult<Float, AppExceptionDomainModel>> =
        repository.downloadRegion(regionId)
}