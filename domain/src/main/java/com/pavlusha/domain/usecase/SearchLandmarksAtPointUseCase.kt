package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkSearchRepository

class SearchLandmarksAtPointUseCase(
    private val searchRepository: ILandmarkSearchRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double
    ): TResult<List<SearchResultDomainModel>, AppExceptionDomainModel> {
        return searchRepository.searchAtPoint(lat, lon)
    }
}