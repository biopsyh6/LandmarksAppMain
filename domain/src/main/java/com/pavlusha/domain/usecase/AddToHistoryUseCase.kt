package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.VisitHistoryDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.IUserContentRepository
import java.util.UUID

class AddToHistoryUseCase(
    private val userContentRepository: IUserContentRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(
        landmarkId: String,
        durationSeconds: Int
    ): TResult<Unit, AppExceptionDomainModel> {
        val user = authRepository.getCurrentUser()

        if (user == null) {
            return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("Login to save history")))
        }

        val visit = VisitHistoryDomainModel(
            id = UUID.randomUUID().toString(),
            userId = user.id,
            landmarkId = landmarkId,
            visitDate = System.currentTimeMillis(),
            durationSeconds = durationSeconds
        )

        return userContentRepository.addToHistory(visit)
    }
}