package com.pavlusha.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pavlusha.data.model.remote.ARContentRemoteModel
import com.pavlusha.data.model.remote.GeoFenceRemoteModel
import com.pavlusha.data.model.remote.LandmarkRemoteModel
import kotlinx.coroutines.tasks.await

class LandmarkRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    private val landmarksCollection = firestore.collection("landmarks")
    private val arConfigsCollection = firestore.collection("ar_configs")
    private val geoFencesCollection = firestore.collection("geo_fences")

    suspend fun getAllLandmarks(): List<LandmarkRemoteModel> {
        return try {
            val snapshot = landmarksCollection.get().await()
            snapshot.toObjects(LandmarkRemoteModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getLandmarkById(id: String): LandmarkRemoteModel? {
        return try {
            val snapshot = landmarksCollection.document(id).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(LandmarkRemoteModel::class.java)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getARConfig(landmarkId: String): ARContentRemoteModel? {
        return try {
            val snapshot = arConfigsCollection.document(landmarkId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(ARContentRemoteModel::class.java)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getGeoFences(): List<GeoFenceRemoteModel> {
        return try {
            val snapshot = geoFencesCollection.get().await()
            snapshot.toObjects(GeoFenceRemoteModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}