package com.pavlusha.domain.usecase

import com.pavlusha.domain.repository.IAuthRepository

class SignInUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.signIn(email, password)
}