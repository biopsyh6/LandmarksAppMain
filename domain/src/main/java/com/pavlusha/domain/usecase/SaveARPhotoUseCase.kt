package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserARPhotoDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.IUserContentRepository

class SaveARPhotoUseCase(
    private val repository: IUserContentRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(
        landmarkId: String,
        imageBytes: ByteArray
    ): TResult<UserARPhotoDomainModel, AppExceptionDomainModel> {
        if (imageBytes.isEmpty()) {
            return TResult.Error(
                AppExceptionDomainModel.Other(Exception("Image data is empty"))
            )
        }

        val user = authRepository.getCurrentUser()
        if (user == null) {
            return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("User not logged in")))
        }

        return repository.saveARPhoto(landmarkId, user.id, imageBytes)
    }
}