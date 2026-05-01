package com.pavlusha.data.remote

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class YandexSearchDataSource(
    private val searchManager: SearchManager
) {
    suspend fun fetchLandmarks(query: String, geometry: Geometry): List<GeoObject> =
        suspendCancellableCoroutine {
            continuation ->
            val searchOptions = SearchOptions().apply {
                searchTypes = SearchType.BIZ.value or SearchType.GEO.value
                resultPageSize = 20
            }

            val session = searchManager.submit(
                query,
                geometry,
                searchOptions,
                object : Session.SearchListener {
                    override fun onSearchResponse(response: Response) {
                        val items = response.collection.children.mapNotNull { it.obj }
                        continuation.resume(items)
                    }

                    override fun onSearchError(error: Error) {
                        continuation.resumeWithException(Exception("Yandex Search Error: $error"))
                    }
                }
            )

            continuation.invokeOnCancellation { session.cancel() }
        }
}