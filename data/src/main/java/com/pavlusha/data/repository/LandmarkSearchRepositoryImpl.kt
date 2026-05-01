package com.pavlusha.data.repository

import com.pavlusha.data.mapper.SearchDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.remote.YandexSearchDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.LandmarkSource
import com.pavlusha.domain.model.SearchResultDomainModel
import com.pavlusha.domain.model.UserLocationDomainModel
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
    private val searchManager: SearchManager
) : ILandmarkSearchRepository {
    override suspend fun searchLandmarks(
        query: String,
        userLocation: UserLocationDomainModel?
    ): TResult<List<SearchResultDomainModel>, AppExceptionDomainModel> {
        return try {
            val point = userLocation?.let { Point(it.latitude, it.longitude) } ?: Point(0.0, 0.0)
            val geometry = Geometry.fromPoint(point)

            val finalQuery = if (query.length < 3) query else "$query достопримечательности"
            val geoObjects = searchDataSource.fetchLandmarks(finalQuery, geometry)

            if (geoObjects.isEmpty()) {
                TResult.Error(AppExceptionDomainModel.EmptySearchResult(Exception("Nothing found")))
            } else {
                val domainList =
                    geoObjects.map { SearchDataMapper.fromYandexToSearchResultDomain(it) }
                TResult.Success(domainList)
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
            val domainList = geoObjects.map { SearchDataMapper.fromYandexToSearchResultDomain(it) }
            TResult.Success(domainList)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getExternalLandmarkDetails(
        id: String,
        source: LandmarkSource
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel> =
        suspendCancellableCoroutine { continuation ->
            val session = searchManager.resolveURI(
                id,
                SearchOptions(),
                object : Session.SearchListener {
                    override fun onSearchResponse(response: Response) {
                        val geoObject = response.collection.children.firstOrNull()?.obj
                        if (geoObject != null) {
                            val domainModel = SearchDataMapper.fromYandexToLandmarkDomain(geoObject)
                            continuation.resume(TResult.Success(domainModel))
                        } else {
                            continuation.resume(
                                TResult.Error(
                                    AppExceptionDomainModel.NotFound(
                                        Exception("Object not found")
                                    )
                                )
                            )
                        }
                    }

                    override fun onSearchError(error: Error) {
                        continuation.resume(TResult.Error(error.toAppExceptionDomainModel()))
                    }
                }
            )

            continuation.invokeOnCancellation { session.cancel() }
        }
}