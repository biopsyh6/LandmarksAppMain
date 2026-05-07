package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserARPhotoDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IUserContentRepository

class GetUserGalleryUseCase(
    private val repository: IUserContentRepository
) {
    suspend operator fun invoke(): TResult<List<UserARPhotoDomainModel>, AppExceptionDomainModel> {
        return repository.getUserGallery()
    }
}