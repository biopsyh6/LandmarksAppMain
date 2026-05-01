package com.pavlusha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pavlusha.data.local.entity.ExternalInfoEntity
import com.pavlusha.data.local.entity.HistoricalPeriodEntity
import com.pavlusha.data.local.entity.LandmarkEntity
import com.pavlusha.data.local.entity.LandmarkGalleryEntity
import com.pavlusha.data.local.entity.LandmarkSourceUrlEntity
import com.pavlusha.data.local.entity.LandmarkTagEntity
import com.pavlusha.data.local.entity.relations.LandmarkWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface LandmarkDao {
    // Read

    @Transaction
    @Query("SELECT * FROM landmarks WHERE id = :id")
    suspend fun getLandmarkById(id: String): LandmarkWithDetails?

    @Transaction
    @Query("SELECT * FROM landmarks WHERE name LIKE '%' || :query || '%'")
    suspend fun searchLandmarks(query: String): List<LandmarkWithDetails>

    @Transaction
    @Query("SELECT * FROM landmarks")
    fun gelAllLandmarksFlow(): Flow<List<LandmarkWithDetails>>

    @Transaction
    @Query("SELECT * FROM landmarks WHERE isFavorite = 1")
    suspend fun getFavoriteLandmarks(): List<LandmarkWithDetails>


    // Write

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLandmarkBase(landmark: LandmarkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriods(periods: List<HistoricalPeriodEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGallery(images: List<LandmarkGalleryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<LandmarkTagEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSourceUrls(urls: List<LandmarkSourceUrlEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExternalInfo(info: List<ExternalInfoEntity>)

    @Transaction
    suspend fun insertFullLandmark(
        landmark: LandmarkEntity,
        periods: List<HistoricalPeriodEntity>,
        gallery: List<LandmarkGalleryEntity>,
        tags: List<LandmarkTagEntity>,
        sources: List<LandmarkSourceUrlEntity>,
        externalInfo: List<ExternalInfoEntity>
    ) {
        insertLandmarkBase(landmark)
        insertPeriods(periods)
        insertGallery(gallery)
        insertTags(tags)
        insertSourceUrls(sources)
        insertExternalInfo(externalInfo)
    }

    // update delete
    @Query("UPDATE landmarks SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)

    @Query("DELETE FROM landmarks WHERE id = :id")
    suspend fun deleteLandmarkById(id: String)
}