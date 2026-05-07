package com.pavlusha.data.repository

import android.location.Location
import com.pavlusha.data.local.dao.LandmarkDao
import com.pavlusha.data.mapper.LandmarkLocalDataMapper
import com.pavlusha.data.mapper.SearchDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.remote.WikipediaDataSource
import com.pavlusha.data.remote.YandexSearchDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.UserLocationDomainModel
import com.pavlusha.domain.model.WikipediaInfoDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkSearchRepository
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LandmarkSearchRepositoryImpl(
    private val searchDataSource: YandexSearchDataSource,
    private val wikipediaDataSource: WikipediaDataSource,
    private val landmarkDao: LandmarkDao
) : ILandmarkSearchRepository {
    override suspend fun searchLandmarks(
        query: String,
        userLocation: UserLocationDomainModel?
    ): TResult<List<SearchResultDomainModel>, AppExceptionDomainModel> {
        return try {
            val point = userLocation?.let { Point(it.latitude, it.longitude) } ?: Point(0.0, 0.0)
            val geometry = Geometry.fromPoint(point)

//            val finalQuery = if (query.length < 3) query else "$query достопримечательности"
            val geoObjects = searchDataSource.fetchLandmarks(query, geometry)

            if (geoObjects.isEmpty()) {
                TResult.Error(AppExceptionDomainModel.EmptySearchResult(Exception("Nothing found")))
            } else {
                val domainList =
                    geoObjects.map { geoObj ->
                        var domainModel = SearchDataMapper.fromYandexToSearchResultDomain(geoObj)

                        if (userLocation != null) {
                            val distance = FloatArray(1)
                            Location.distanceBetween(
                                userLocation.latitude,
                                userLocation.longitude,
                                domainModel.latitude,
                                domainModel.longitude,
                                distance
                            )
                            domainModel = domainModel.copy(distanceMeters = distance[0])
                        }
                        domainModel
                    }
                TResult.Success(domainList.sortedBy { it.distanceMeters ?: Float.MAX_VALUE })
            }
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun searchAtPoint(
        lat: Double,
        lon: Double,
        zoom: Int
    ): TResult<List<SearchResultDomainModel>, AppExceptionDomainModel> {
        return try {
            val geometry = Geometry.fromPoint(Point(lat, lon))
            val geoObjects = searchDataSource.fetchLandmarks("достопримечательности", geometry)
            val domainList = geoObjects.map { geoObj ->
                var domainModel = SearchDataMapper.fromYandexToSearchResultDomain(geoObj)

                val distance = FloatArray(1)
                Location.distanceBetween(lat, lon, domainModel.latitude, domainModel.longitude, distance)
                domainModel = domainModel.copy(distanceMeters = distance[0])

                domainModel
            }
            TResult.Success(domainList.sortedBy { it.distanceMeters ?: Float.MAX_VALUE })
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getExternalLandmarkDetails(
        id: String,
        source: LandmarkSource
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel> {
        return try {
            val geoObject = searchDataSource.fetchLandmarkDetailsByUri(id)

            if (geoObject != null) {
                val domainModel = SearchDataMapper.fromYandexToLandmarkDomain(geoObject)
                saveToLocalCache(domainModel)

                TResult.Success(domainModel)
            } else {
                TResult.Error(AppExceptionDomainModel.NotFound(Exception("Object not found in Yandex")))
            }
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getWikipediaDescription(landmarkName: String): TResult<WikipediaInfoDomainModel, AppExceptionDomainModel> {
        return try {
            val response = wikipediaDataSource.getArticleSummary(landmarkName)

            if (response?.extract != null && response.title != null) {

                val domainModel = WikipediaInfoDomainModel(
                    title = response.title,
                    extract = response.extract,
                    thumbnailUrl = response.thumbnail?.source,
                    originalImageUrl = response.originalImage?.source,
                    mobileArticleUrl = response.contentUrls?.mobile?.page
                )

                TResult.Success(domainModel)
            } else {
                TResult.Error(AppExceptionDomainModel.NotFound(Exception("Article not found in Wikipedia")))
            }
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    private suspend fun saveToLocalCache(domain: LandmarkDomainModel) {
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
}