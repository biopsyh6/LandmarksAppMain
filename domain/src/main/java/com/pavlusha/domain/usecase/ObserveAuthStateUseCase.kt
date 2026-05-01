package com.pavlusha.domain.usecase

import com.pavlusha.domain.model.UserDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val repository: IAuthRepository
) {
    operator fun invoke(): Flow<UserDomainModel?> =
        repository.observeAuthState()
}