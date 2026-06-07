package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ILandmarkRepository

class GetFavoriteLandmarksUseCase(
    private val repository: ILandmarkRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): TResult<List<LandmarkDomainModel>, AppExceptionDomainModel> {
        val user = authRepository.getCurrentUser()
        if (user == null) {
            return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("Пользователь не авторизован")))
        }
        return repository.getFavoriteLandmarks()
    }
}