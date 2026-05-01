package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository

class SearchLocalLandmarksUseCase(
    private val repository: ILandmarkRepository
) {
    suspend operator fun invoke(query: String): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel> {
        if (query.length < 2) return TResult.Success(emptyList())
        return repository.searchLandmarks(query)
    }
}