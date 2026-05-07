package com.pavlusha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavlusha.data.local.entity.UserARPhotoEntity
import com.pavlusha.data.local.entity.UserNoteEntity
import com.pavlusha.data.local.entity.VisitHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserContentDao {
    // Notes

    @Query("SELECT * FROM user_notes WHERE landmarkId = :landmarkId ORDER BY updatedAt DESC")
    suspend fun getNotesByLandmarkId(landmarkId: String): List<UserNoteEntity>

    @Query("SELECT * FROM user_notes ORDER BY updatedAt DESC")
    fun getAllNotesFlow(): Flow<List<UserNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: UserNoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<UserNoteEntity>)

    @Query("DELETE FROM user_notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("DELETE FROM user_notes")
    suspend fun clearAllNotes()

    // Visit History

    @Query("SELECT * FROM visit_history WHERE landmarkId = :landmarkId ORDER BY visitDate DESC")
    suspend fun getVisitsByLandmarkId(landmarkId: String): List<VisitHistoryEntity>

    @Query("SELECT * FROM visit_history ORDER BY visitDate DESC")
    fun getAllVisitsFlow(): Flow<List<VisitHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisits(visits: List<VisitHistoryEntity>)

    @Query("DELETE FROM visit_history WHERE id = :visitId")
    suspend fun deleteVisitById(visitId: String)

    @Query("DELETE FROM visit_history")
    suspend fun clearAllVisits()

    @Query("SELECT * FROM user_ar_photos ORDER BY createdAt DESC")
    suspend fun getAllARPhotos(): List<UserARPhotoEntity>

    @Query("SELECT * FROM user_ar_photos WHERE id = :photoId LIMIT 1")
    suspend fun getARPhotoById(photoId: String): UserARPhotoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertARPhoto(photo: UserARPhotoEntity)

    @Query("DELETE FROM user_ar_photos WHERE id = :photoId")
    suspend fun deleteARPhotoById(photoId: String)
}