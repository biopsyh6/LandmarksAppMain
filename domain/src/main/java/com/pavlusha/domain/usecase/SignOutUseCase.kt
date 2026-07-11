package com.pavlusha.domain.usecase

import com.pavlusha.domain.repository.IAuthRepository

class SignOutUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke() = repository.signOut()
}