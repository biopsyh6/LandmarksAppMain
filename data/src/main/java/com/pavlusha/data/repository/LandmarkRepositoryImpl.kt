package com.pavlusha.data.repository

import android.location.Location
import com.pavlusha.data.local.dao.LandmarkDao
import com.pavlusha.data.mapper.LandmarkLocalDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.mapper.remote.LandmarkRemoteDataMapper
import com.pavlusha.data.remote.LandmarkRemoteDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.RecognitionResultDomainModel
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository
import kotlinx.coroutines.flow.Flow

class LandmarkRepositoryImpl(
    private val landmarkDao: LandmarkDao,
    private val remoteDataSource: LandmarkRemoteDataSource
) : ILandmarkRepository {
    override suspend fun getNearbyLandmarks(
        lat: Double,
        lon: Double,
        radiusMeters: Float
    ): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel> {
        return try {
            val initialCheck = landmarkDao.searchLandmarks("")
            if (initialCheck.isEmpty()) {
                syncAllLandmarks()
            }

            val allLocal = landmarkDao.searchLandmarks("")

            val filtered = allLocal.mapNotNull { relation ->
                val distance = FloatArray(1)
                Location.distanceBetween(
                    lat,
                    lon,
                    relation.landmark.latitude,
                    relation.landmark.longitude,
                    distance
                )

                if (distance[0] <= radiusMeters) {
                    LandmarkLocalDataMapper.toDomainFromData(relation).copy(distanceMeters = distance[0])
                } else {
                    null
                }
            }.sortedBy { it.distanceMeters }

            TResult.Success(filtered)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getLandmarkById(id: String): TResult<LandmarkDomainModel, AppExceptionDomainModel> {
        return try {
            val remoteData = remoteDataSource.getLandmarkById(id)

            if (remoteData != null) {
                var domainModel = LandmarkRemoteDataMapper.toDomainFromData(remoteData)

                val existingLocal = landmarkDao.getLandmarkById(id)
                if (existingLocal != null) {
                    val localDomain = LandmarkLocalDataMapper.toDomainFromData(existingLocal)
                    domainModel = domainModel.copy(
                        isFavorite = localDomain.isFavorite,
                        isAvailableOffline = localDomain.isAvailableOffline,
                        localModel3dPath = localDomain.localModel3dPath,
                        localMainImagePath = localDomain.localMainImagePath
                    )
                }

                saveDomainToLocalDb(domainModel)
            }

            val finalLocalData = landmarkDao.getLandmarkById(id)
            if (finalLocalData != null) {
                TResult.Success(LandmarkLocalDataMapper.toDomainFromData(finalLocalData))
            } else {
                TResult.Error(AppExceptionDomainModel.NotFound(Exception("Landmark not found in local DB or Network")))
            }
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getLandmarkByRecognition(result: RecognitionResultDomainModel): TResult<LandmarkDomainModel, AppExceptionDomainModel> {

        val targetId = result.landmarkId ?: result.className
        return getLandmarkById(targetId)
//        if (result.landmarkId != null) {
//            return getLandmarkById(result.landmarkId!!)
//        }
//
//        val searchQuery = mapClassNameToDbQuery(result.className)
//
//        val searchResult = searchLandmarks(searchQuery)
//
//        return when (searchResult) {
//            is TResult.Success -> {
//                val landmark = searchResult.data.firstOrNull()
//                if (landmark != null) {
//                    TResult.Success(landmark)
//                } else {
//                    TResult.Error(AppExceptionDomainModel.NotFound(Exception("No landmark for class: ${result.className}")))
//                }
//            }
//
//            is TResult.Error -> {
//                TResult.Error(searchResult.exception)
//            }
//        }
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

    override suspend fun getFavoriteLandmarks(): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel> {
        return try {
            val favoritesLocal = landmarkDao.getFavoriteLandmarks()
            val domainList = favoritesLocal.map { LandmarkLocalDataMapper.toDomainFromData(it) }
            TResult.Success(domainList)
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

    private suspend fun saveDomainToLocalDb(domain: LandmarkDomainModel) {
        val dataToSave = LandmarkLocalDataMapper.fromDomainToData(domain)
        landmarkDao.insertFullLandmark(
            landmark = dataToSave.landmark,
            periods = dataToSave.periods,
            gallery = dataToSave.gallery,
            tags = dataToSave.tags,
            sources = dataToSave.sourceUrls,
            externalInfo = dataToSave.externalInfo
        )
    }

    private suspend fun syncAllLandmarks() {
        try {
            val allRemote = remoteDataSource.getAllLandmarks()
            allRemote.forEach { remoteModel ->
                val domainModel = LandmarkRemoteDataMapper.toDomainFromData(remoteModel)
                saveDomainToLocalDb(domainModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun mapClassNameToDbQuery(className: String): String {
        return when(className) {
            "island_of_tears" -> "Остров Мужества и Скорби"
            "library" -> "Национальная библиотека Беларуси"
            "mir" -> "Мирский замок"
            "bigben" -> "Биг-Бен"
            "church_nemiga" -> "Свято-Духов собор (Минск)"
            "colosseum" -> "Колизей"
            "eiffel" -> "Эйфелева башня"
            "isaac" -> "Исаакиевский собор"
            "minsk_gates" -> "Ворота Минска"
            "pisa" -> "Пизанская башня"
            "sphinx" -> "Большой сфинкс"
            "taj_mahal" -> "Тадж-Махал"
            "tower_bridge" -> "Тауэрский мост"
            "townhall" -> "Минская ратуша"
            else -> className.replace("_", " ")
        }
    }
}