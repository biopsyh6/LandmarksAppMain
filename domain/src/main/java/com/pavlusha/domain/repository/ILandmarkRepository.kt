package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.RecognitionResultDomainModel
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import kotlinx.coroutines.flow.Flow

interface ILandmarkRepository {
    suspend fun getNearbyLandmarks(
        lat: Double,
        lon: Double,
        radiusMeters: Float,
    ): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel>

    suspend fun getLandmarkById(
        id: String
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel>

    suspend fun getLandmarkByRecognition(
        result: RecognitionResultDomainModel
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel>

    suspend fun toggleFavourite(
        landmarkId: String,
        isFavourite: Boolean,
    ): TResult<Unit, AppExceptionDomainModel>

    suspend fun getFavoriteLandmarks(): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel>

    suspend fun searchLandmarks(query: String): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel>
}