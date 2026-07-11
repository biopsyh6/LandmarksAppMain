package com.pavlusha.domain.usecase

import com.pavlusha.domain.repository.IRegionRepository

class GetDownloadedRegionsUseCase(
    private val repository: IRegionRepository
) {
    suspend operator fun invoke() = repository.getDownloadedRegions()
}