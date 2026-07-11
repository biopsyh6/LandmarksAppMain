package com.pavlusha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavlusha.data.local.entity.GeoFenceEntity

@Dao
interface GeoFenceDao {
    @Query("SELECT * FROM geo_fences")
    suspend fun getAllGeoFences(): List<GeoFenceEntity>

    @Query("SELECT * FROM geo_fences WHERE landmarkId = :landmarkId LIMIT 1")
    suspend fun getGeoFenceByLandmarkId(landmarkId: String): GeoFenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeoFence(geoFence: GeoFenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeoFences(geoFences: List<GeoFenceEntity>)

    @Query("DELETE FROM geo_fences WHERE landmarkId = :landmarkId")
    suspend fun deleteGeoFenceByLandmarkId(landmarkId: String)

    @Query("DELETE FROM geo_fences")
    suspend fun clearAllGeoFences()
}