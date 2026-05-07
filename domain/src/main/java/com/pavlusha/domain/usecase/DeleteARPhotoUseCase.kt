package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IUserContentRepository

class DeleteARPhotoUseCase(
    private val repository: IUserContentRepository
) {
    suspend operator fun invoke(
        photoId: String
    ): TResult<Unit, AppExceptionDomainModel> {
        if (photoId.isBlank()) {
            return TResult.Error(
                AppExceptionDomainModel.Other(Exception("Photo ID cannot be empty"))
            )
        }
        return repository.deleteARPhoto(photoId)
    }
}