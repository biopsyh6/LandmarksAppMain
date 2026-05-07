package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserARPhotoDomainModel
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.VisitHistoryDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel

interface IUserContentRepository {
    suspend fun saveNote(
        note: UserNoteDomainModel
    ): TResult<Unit, AppExceptionDomainModel>

    suspend fun getNotesForLandmark(
        landmarkId: String
    ): TResult<List<UserNoteDomainModel>, AppExceptionDomainModel>

    suspend fun addToHistory(
        visit: VisitHistoryDomainModel
    ): TResult<Unit, AppExceptionDomainModel>

    suspend fun saveARPhoto(
        landmarkId: String,
        imageBytes: ByteArray
    ): TResult<UserARPhotoDomainModel, AppExceptionDomainModel>

    suspend fun getUserGallery(): TResult<List<UserARPhotoDomainModel>, AppExceptionDomainModel>

    suspend fun deleteARPhoto(photoId: String): TResult<Unit, AppExceptionDomainModel>
}