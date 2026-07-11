package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.UserLocationDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.ILandmarkSearchRepository

class SearchLandmarksUseCase(
    private val searchRepository: ILandmarkSearchRepository,
    private val localRepository: ILandmarkRepository
) {
    suspend operator fun invoke(
        query: String,
        userLocation: UserLocationDomainModel?
    ): TResult<List<SearchResultDomainModel>, AppExceptionDomainModel> {
        val searchResult = searchRepository.searchLandmarks(query, userLocation)

        if (searchResult is TResult.Success) {
            val localResult = localRepository.searchLandmarks(query)

            val enrichedResults = searchResult.data.map { remoteItem ->
                val isInLocalDb = (localResult as? TResult.Success)?.data?.any { local ->
                    local.name.equals(remoteItem.name, ignoreCase = true)
                } ?: false

                remoteItem.copy(isPromoted = isInLocalDb)
            }
            return TResult.Success(enrichedResults)
        }
        return searchResult
    }
}