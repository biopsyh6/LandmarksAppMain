package com.pavlusha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pavlusha.data.local.entity.ARAnnotationEntity
import com.pavlusha.data.local.entity.ARContentConfigEntity
import com.pavlusha.data.local.entity.relations.ARDataWithDetails

@Dao
interface ARContentDao {
    // Read

    @Transaction
    @Query("SELECT * FROM ar_content_configs WHERE landmarkId = :landmarkId")
    suspend fun getARContentByLandmarkId(landmarkId: String): ARDataWithDetails?

    @Query("SELECT * FROM ar_content_configs WHERE landmarkId = :landmarkId")
    suspend fun getARConfigById(landmarkId: String): ARContentConfigEntity?

    @Query("SELECT * FROM ar_annotations WHERE landmarkId = :landmarkId")
    suspend fun getAnnotationsByLandmarkId(landmarkId: String): List<ARAnnotationEntity>

    // Write

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertARConfig(config: ARContentConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnotations(annotations: List<ARAnnotationEntity>)

    @Query("DELETE FROM ar_annotations WHERE landmarkId = :landmarkId")
    suspend fun deleteAnnotationsByLandmarkId(landmarkId: String)

    @Transaction
    suspend fun insertFullARContent(
        config: ARContentConfigEntity,
        annotations: List<ARAnnotationEntity>
    ) {
        insertARConfig(config)
        deleteAnnotationsByLandmarkId(config.landmarkId)
        insertAnnotations(annotations)
    }

    // Delete

    @Query("DELETE FROM ar_content_configs WHERE landmarkId = :landmarkId")
    suspend fun deleteARConfigByLandmarkId(landmarkId: String)
}