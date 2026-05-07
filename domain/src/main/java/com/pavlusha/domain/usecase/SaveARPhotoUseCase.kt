package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserARPhotoDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IUserContentRepository

class SaveARPhotoUseCase(
    private val repository: IUserContentRepository
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

        return repository.saveARPhoto(landmarkId, imageBytes)
    }
}