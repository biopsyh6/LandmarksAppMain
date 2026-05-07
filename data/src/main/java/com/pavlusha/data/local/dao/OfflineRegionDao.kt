package com.pavlusha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavlusha.data.local.entity.RegionPackageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineRegionDao {

    @Query("SELECT * FROM offline_regions")
    fun getAllRegionsFlow(): Flow<List<RegionPackageEntity>>

    @Query("SELECT * FROM offline_regions WHERE regionId = :regionId LIMIT 1")
    suspend fun getRegionById(regionId: String): RegionPackageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegion(region: RegionPackageEntity)

    @Query("UPDATE offline_regions SET status = :status, progress = :progress WHERE regionId = :regionId")
    suspend fun updateRegionStatus(regionId: String, status: String, progress: Float)

    @Query("DELETE FROM offline_regions WHERE regionId = :regionId")
    suspend fun deleteRegionById(regionId: String)
}