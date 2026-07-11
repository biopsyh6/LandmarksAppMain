package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository

class SignInWithGoogleUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(
        idToken: String
    ): TResult<UserDomainModel, AppExceptionDomainModel> {
        if (idToken.isBlank()) {
            return TResult.Error(AppExceptionDomainModel.Other(Exception("Google ID Token is empty")))
        }

        return repository.signInWithGoogle(idToken)
    }
}