package com.pavlusha.data.remote

import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.transport.masstransit.FitnessOptions
import com.yandex.mapkit.transport.masstransit.PedestrianRouter
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.RouteOptions
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class YandexRoutingDataSource(
    private val pedestrianRouter: PedestrianRouter
) {

    private var currentSession: Session? = null
    suspend fun fetchPedestrianRoute(start: Point, end: Point): Polyline? =
        suspendCancellableCoroutine { continuation ->
            val points = listOf(
                RequestPoint(start, RequestPointType.WAYPOINT, null, null, null),
                RequestPoint(end, RequestPointType.WAYPOINT, null, null, null)
            )

            val timeOptions = TimeOptions(null, null)
            val fitnessOptions = FitnessOptions(false, false)
            val routeOptions = RouteOptions(fitnessOptions)

            currentSession = pedestrianRouter.requestRoutes(
                points,
                timeOptions,
                routeOptions,
                object : Session.RouteListener {
                    override fun onMasstransitRoutes(routes: MutableList<Route>) {
                        if (routes.isNotEmpty()) {
                            continuation.resume(routes.first().geometry)
                        } else {
                            continuation.resume(null)
                        }
                        currentSession = null
                    }

                    override fun onMasstransitRoutesError(error: com.yandex.runtime.Error) {
                        continuation.resumeWithException(Exception("Routing error: $error"))
                        currentSession = null
                    }
                }
            )

            continuation.invokeOnCancellation {
                currentSession?.cancel()
                currentSession = null
            }
        }
}