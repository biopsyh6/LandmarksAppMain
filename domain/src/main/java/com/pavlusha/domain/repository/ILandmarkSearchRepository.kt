package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.UserLocationDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel

interface ILandmarkSearchRepository {
    suspend fun searchLandmarks(
        query: String,
        userLocation: UserLocationDomainModel? = null
    ): TResult<List<SearchResultDomainModel>, AppExceptionDomainModel>

    suspend fun searchAtPoint(
        lat: Double,
        lon: Double,
        zoom: Int = 15
    ): TResult<List<SearchResultDomainModel>, AppExceptionDomainModel>

    suspend fun getExternalLandmarkDetails(
        id: String,
        source: LandmarkSource
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel>
}