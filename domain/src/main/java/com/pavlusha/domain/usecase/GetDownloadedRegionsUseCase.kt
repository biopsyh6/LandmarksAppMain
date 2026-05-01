package com.pavlusha.domain.usecase

import com.pavlusha.domain.repository.ILandmarkRepository

class GetDownloadedRegionsUseCase(
    private val repository: ILandmarkRepository
) {
    suspend operator fun invoke() = repository.getDownloadedRegions()
}