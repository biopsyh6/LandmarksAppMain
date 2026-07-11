package com.pavlusha.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pavlusha.data.model.remote.RegionPackageRemoteModel
import kotlinx.coroutines.tasks.await

class RegionRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    private val regionsCollection = firestore.collection("offline_regions")

    suspend fun getAvailableRegions(): List<RegionPackageRemoteModel> {
        return try {
            val snapshot = regionsCollection.get().await()
            snapshot.toObjects(RegionPackageRemoteModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getRegionById(regionId: String): RegionPackageRemoteModel? {
        return try {
            val snapshot = regionsCollection.document(regionId).get().await()
            if (snapshot.exists()) snapshot.toObject(RegionPackageRemoteModel::class.java) else null
        } catch (e: Exception) {
            null
        }
    }
}