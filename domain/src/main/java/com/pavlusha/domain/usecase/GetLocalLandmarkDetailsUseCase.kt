package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository

class GetLocalLandmarkDetailsUseCase(
    private val repository: ILandmarkRepository
) {
    suspend operator fun invoke(
        id: String
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel> =
        repository.getLandmarkById(id)
}