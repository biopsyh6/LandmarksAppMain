package com.pavlusha.domain.usecase

import com.pavlusha.domain.repository.ICompassRepository
import kotlinx.coroutines.flow.Flow

class ObserveCompassHeadingUseCase(
    private val compassRepository: ICompassRepository
) {
    operator fun invoke(): Flow<Float> = compassRepository.observeHeading()
}