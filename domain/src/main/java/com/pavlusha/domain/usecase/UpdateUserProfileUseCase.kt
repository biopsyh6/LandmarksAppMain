package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository

class UpdateUserProfileUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(
        user: UserDomainModel
    ): TResult<Unit, AppExceptionDomainModel> {
        if (user.displayName != null && user.displayName.length > 30) {
            return TResult.Error(
                AppExceptionDomainModel.Other(Exception("The name is too long"))
            )
        }

        return repository.updateUserProfile(user)
    }
}