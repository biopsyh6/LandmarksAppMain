package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ILandmarkRepository

class ToggleFavoriteUseCase(
    private val landmarkRepository: ILandmarkRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(
        landmarkId: String, isFavourite: Boolean
    ): TResult<Unit, AppExceptionDomainModel> {
        if (authRepository.getCurrentUser() == null) {
            return TResult.Error(AppExceptionDomainModel.NoAuth(Exception()))
        }
        return landmarkRepository.toggleFavourite(landmarkId, isFavourite)
    }
}