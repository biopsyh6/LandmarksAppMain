package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserNoteDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.IUserContentRepository
import java.util.UUID

class SaveLandmarkNoteUseCase(
    private val userContentRepository: IUserContentRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(
        landmarkId: String,
        text: String
    ): TResult<Unit, AppExceptionDomainModel> {
        val user = authRepository.getCurrentUser()

        if (user == null) {
            return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("User not logged in")))
        }

        val note = UserNoteDomainModel(
            id = UUID.randomUUID().toString(),
            landmarkId = landmarkId,
            text = text,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        return userContentRepository.saveNote(note)
    }
}