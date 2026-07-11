package com.pavlusha.data.repository

import com.pavlusha.data.local.dao.UserContentDao
import com.pavlusha.data.mapper.UserContentLocalDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.mapper.remote.UserContentRemoteDataMapper
import com.pavlusha.data.remote.LocalPhotoStorageDataSource
import com.pavlusha.data.remote.UserRemoteDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserARPhotoDomainModel
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.VisitHistoryDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IUserContentRepository
import kotlinx.coroutines.flow.first
import java.io.File
import java.util.UUID

class UserContentRepositoryImpl(
    private val userContentDao: UserContentDao,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val photoStorageDataSource: LocalPhotoStorageDataSource,
) : IUserContentRepository {
    override suspend fun saveNote(note: UserNoteDomainModel): TResult<Unit, AppExceptionDomainModel> {
        return try {
            val localEntity = UserContentLocalDataMapper.fromDomainToData(note)
            userContentDao.insertNote(localEntity)

            val remoteModel = UserContentRemoteDataMapper.fromDomainToData(note)
            userRemoteDataSource.saveUserNote(note.userId, remoteModel)

            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getNotesForLandmark(
        landmarkId: String,
        userId: String
    ): TResult<List<UserNoteDomainModel>, AppExceptionDomainModel> {
        return try {
            try {
                val remoteNotes = userRemoteDataSource.getUserNotes(userId)
                val landmarkNotesRemote = remoteNotes.filter { it.landmarkId == landmarkId }

                val entitiesToSave = landmarkNotesRemote.map { remoteModel ->
                    val domainModel = UserContentRemoteDataMapper.toDomainFromData(remoteModel)
                    UserContentLocalDataMapper.fromDomainToData(domainModel)
                }
                if (entitiesToSave.isNotEmpty()) {
                    userContentDao.insertNotes(entitiesToSave)
                }
            } catch (e: Exception) {
            }

            val localNotes = userContentDao.getNotesByLandmarkId(landmarkId, userId)
            val domainNotes = localNotes.map { UserContentLocalDataMapper.toDomainFromData(it) }

            TResult.Success(domainNotes)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getAllUserNotes(userId: String): TResult<List<UserNoteDomainModel>, AppExceptionDomainModel> {
        return try {
            try {
                val remoteNotes = userRemoteDataSource.getUserNotes(userId)
                if (remoteNotes.isNotEmpty()) {
                    val entitiesToSave = remoteNotes.map { remoteModel ->
                        val domainModel = UserContentRemoteDataMapper.toDomainFromData(remoteModel)
                        UserContentLocalDataMapper.fromDomainToData(domainModel)
                    }
                    userContentDao.insertNotes(entitiesToSave)
                }
            } catch (e: Exception) { e.printStackTrace() }

            val localNotes = userContentDao.getAllNotesFlow(userId).first()
            val domainNotes = localNotes.map { UserContentLocalDataMapper.toDomainFromData(it) }

            TResult.Success(domainNotes)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun deleteNote(
        noteId: String,
        userId: String
    ): TResult<Unit, AppExceptionDomainModel> {
        return try {
            userContentDao.deleteNoteById(noteId)
            userRemoteDataSource.deleteUserNote(userId, noteId)
            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun addToHistory(visit: VisitHistoryDomainModel): TResult<Unit, AppExceptionDomainModel> {
        return try {
            val localEntity = UserContentLocalDataMapper.fromDomainToData(visit)
            userContentDao.insertVisit(localEntity)

            val remoteModel = UserContentRemoteDataMapper.fromDomainToData(visit)
            userRemoteDataSource.saveVisitHistory(visit.userId, remoteModel)

            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getUserVisitHistory(userId: String): TResult<List<VisitHistoryDomainModel>, AppExceptionDomainModel> {
        return try {
            try {
                val remoteVisits = userRemoteDataSource.getVisitHistory(userId)
                if (remoteVisits.isNotEmpty()) {
                    val entitiesToSave = remoteVisits.map { remoteModel ->
                        val domainModel = UserContentRemoteDataMapper.toDomainFromData(remoteModel)
                        UserContentLocalDataMapper.fromDomainToData(domainModel)
                    }
                    userContentDao.insertVisits(entitiesToSave)
                }
            } catch (e: Exception) { e.printStackTrace() }

            val localVisits = userContentDao.getAllVisitsFlow(userId).first()
            val domainVisits = localVisits.map { UserContentLocalDataMapper.toDomainFromData(it) }

            TResult.Success(domainVisits)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun saveARPhoto(
        landmarkId: String,
        userId: String,
        imageBytes: ByteArray
    ): TResult<UserARPhotoDomainModel, AppExceptionDomainModel> {
        return try {
            val savedFilePath = photoStorageDataSource.savePhoto(imageBytes)

            val domainModel = UserARPhotoDomainModel(
                id = UUID.randomUUID().toString(),
                userId = userId,
                landmarkId = landmarkId,
                localFilePath = savedFilePath,
                remoteUrl = null,
                createdAt = System.currentTimeMillis()
            )

            val entity = UserContentLocalDataMapper.fromDomainToData(domainModel)
            userContentDao.insertARPhoto(entity)

            TResult.Success(domainModel)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun getUserGallery(
        userId: String
    ): TResult<List<UserARPhotoDomainModel>, AppExceptionDomainModel> {
        return try {
            val localPhotos = userContentDao.getAllARPhotos(userId)
            val domainPhotos = localPhotos.map { UserContentLocalDataMapper.toDomainFromData(it) }

            TResult.Success(domainPhotos)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun deleteARPhoto(photoId: String): TResult<Unit, AppExceptionDomainModel> {
        return try {
            val photoEntity = userContentDao.getARPhotoById(photoId)

            if (photoEntity != null) {
                val file = File(photoEntity.localFilePath)
                if (file.exists()) {
                    file.delete()
                }

                userContentDao.deleteARPhotoById(photoId)
            }

            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }
}