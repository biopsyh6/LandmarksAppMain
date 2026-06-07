package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.IUserContentRepository

class DeleteNoteUseCase(
    private val userContentRepository: IUserContentRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(noteId: String): TResult<Unit, AppExceptionDomainModel> {
        val user = authRepository.getCurrentUser()
            ?: return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("User not logged in")))
        return userContentRepository.deleteNote(noteId, user.id)
    }
}