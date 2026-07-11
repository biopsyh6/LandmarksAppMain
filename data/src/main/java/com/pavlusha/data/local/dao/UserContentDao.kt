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

    @Query("SELECT * FROM user_notes WHERE landmarkId = :landmarkId AND userId = :userId ORDER BY updatedAt DESC")
    suspend fun getNotesByLandmarkId(landmarkId: String, userId: String): List<UserNoteEntity>

    @Query("SELECT * FROM user_notes WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getAllNotesFlow(userId: String): Flow<List<UserNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: UserNoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<UserNoteEntity>)

    @Query("DELETE FROM user_notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: String)

    @Query("DELETE FROM user_notes")
    suspend fun clearAllNotes()

    @Query("DELETE FROM user_notes WHERE userId = :userId")
    suspend fun clearNotesByUser(userId: String)

    // Visit History

    @Query("SELECT * FROM visit_history WHERE landmarkId = :landmarkId AND userId = :userId ORDER BY visitDate DESC")
    suspend fun getVisitsByLandmarkId(landmarkId: String, userId: String): List<VisitHistoryEntity>

    @Query("SELECT * FROM visit_history WHERE userId = :userId ORDER BY visitDate DESC")
    fun getAllVisitsFlow(userId: String): Flow<List<VisitHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: VisitHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisits(visits: List<VisitHistoryEntity>)

    @Query("DELETE FROM visit_history WHERE id = :visitId")
    suspend fun deleteVisitById(visitId: String)

    @Query("DELETE FROM visit_history")
    suspend fun clearAllVisits()

    @Query("DELETE FROM visit_history WHERE userId = :userId")
    suspend fun clearVisitsByUser(userId: String)

    @Query("SELECT * FROM user_ar_photos WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getAllARPhotos(userId: String): List<UserARPhotoEntity>

    @Query("SELECT * FROM user_ar_photos WHERE id = :photoId LIMIT 1")
    suspend fun getARPhotoById(photoId: String): UserARPhotoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertARPhoto(photo: UserARPhotoEntity)

    @Query("DELETE FROM user_ar_photos WHERE id = :photoId")
    suspend fun deleteARPhotoById(photoId: String)

    @Query("DELETE FROM user_ar_photos WHERE userId = :userId")
    suspend fun clearPhotosByUser(userId: String)
}