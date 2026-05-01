package com.pavlusha.data.repository

import android.location.Location
import com.pavlusha.data.local.dao.LandmarkDao
import com.pavlusha.data.mapper.LandmarkLocalDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.RecognitionResultDomainModel
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository
import kotlinx.coroutines.flow.Flow

class LandmarkRepositoryImpl(
    private val landmarkDao: LandmarkDao,
) : ILandmarkRepository {
    override suspend fun getNearbyLandmarks(
        lat: Double,
        lon: Double,
        radiusMeters: Float
    ): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel> {
        return try {
            val delta = radiusMeters / 111000.0

            val allLocal = landmarkDao.searchLandmarks("")

            val filtered = allLocal.filter {
                val distance = FloatArray(1)
                Location.distanceBetween(
                    lat,
                    lon,
                    it.landmark.latitude,
                    it.landmark.longitude,
                    distance
                )
                distance[0] <= radiusMeters
            }.map { LandmarkLocalDataMapper.toDomainFromData(it) }

            TResult.Success(filtered)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getLandmarkById(id: String): TResult<LandmarkDomainModel, AppExceptionDomainModel> {
        return try {
            val result = landmarkDao.getLandmarkById(id)
            if (result != null) {
                TResult.Success(LandmarkLocalDataMapper.toDomainFromData(result))
            } else {
                TResult.Error(AppExceptionDomainModel.NotFound(Exception("Landmark not found in local DB")))
            }
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getLandmarkByRecognition(result: RecognitionResultDomainModel): TResult<LandmarkDomainModel, AppExceptionDomainModel> {
        if (result.landmarkId != null) {
            return getLandmarkById(result.landmarkId!!)
        }

        val searchResult = searchLandmarks(result.className)

        return when (searchResult) {
            is TResult.Success -> {
                val landmark = searchResult.data.firstOrNull()
                if (landmark != null) {
                    TResult.Success(landmark)
                } else {
                    TResult.Error(AppExceptionDomainModel.NotFound(Exception("No landmark for class: ${result.className}")))
                }
            }

            is TResult.Error -> {
                TResult.Error(searchResult.exception)
            }
        }
    }

    override suspend fun toggleFavourite(
        landmarkId: String,
        isFavourite: Boolean
    ): TResult<Unit, AppExceptionDomainModel> {
        return try {
            landmarkDao.updateFavoriteStatus(landmarkId, isFavourite)
            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun searchLandmarks(query: String): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel> {
        return try {
            val results = landmarkDao.searchLandmarks(query)
            TResult.Success(results.map { LandmarkLocalDataMapper.toDomainFromData(it) })
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getAvailableRegions(): TResult<List<RegionPackageDomainModel>, AppExceptionDomainModel> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRegion(regionId: String): TResult<Unit, AppExceptionDomainModel> {
        TODO("Not yet implemented")
    }

    override fun downloadRegion(regionId: String): Flow<TResult<Float, AppExceptionDomainModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDownloadedRegions(): TResult<List<RegionPackageDomainModel>, AppExceptionDomainModel> {
        TODO("Not yet implemented")
    }
}