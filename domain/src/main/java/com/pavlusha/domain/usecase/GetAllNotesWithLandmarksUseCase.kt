package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.NoteWithLandmarkDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.IUserContentRepository

class GetAllNotesWithLandmarksUseCase(
    private val userContentRepository: IUserContentRepository,
    private val landmarkRepository: ILandmarkRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): TResult<List<NoteWithLandmarkDomainModel>, AppExceptionDomainModel> {
        val user = authRepository.getCurrentUser()
            ?: return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("User not logged in")))

        val notesResult = userContentRepository.getAllUserNotes(user.id)
        if (notesResult !is TResult.Success) return TResult.Error(AppExceptionDomainModel.Other(
            Exception("Ошибка при получении заметок пользователя")))

        val enrichedNotes = mutableListOf<NoteWithLandmarkDomainModel>()

        for (note in notesResult.data) {
            val landmarkResult = landmarkRepository.getLandmarkById(note.landmarkId)
            if (landmarkResult is TResult.Success) {
                enrichedNotes.add(NoteWithLandmarkDomainModel(note, landmarkResult.data))
            }
        }

        return TResult.Success(enrichedNotes)
    }
}