package com.pavlusha.domain.usecase

import com.pavlusha.domain.repository.IAuthRepository

class SignUpUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ) = repository.signUp(email, password)
}