package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository

class ResetPasswordUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(
        email: String
    ): TResult<Unit, AppExceptionDomainModel> {
        if (!email.contains("@") || email.length < 5) {
            return TResult.Error(
                AppExceptionDomainModel.InvalidEmail(Exception("Invalid email format"))
            )
        }

        return repository.resetPassword(email)
    }
}