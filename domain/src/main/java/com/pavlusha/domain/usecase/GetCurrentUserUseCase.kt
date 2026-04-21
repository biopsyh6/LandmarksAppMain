package com.pavlusha.domain.usecase

import com.pavlusha.domain.model.UserDomainModel
import com.pavlusha.domain.repository.IAuthRepository

class GetCurrentUserUseCase(
    private val repository: IAuthRepository
) {
    suspend operator fun invoke(): UserDomainModel? {
        return repository.getCurrentUser()
    }
}