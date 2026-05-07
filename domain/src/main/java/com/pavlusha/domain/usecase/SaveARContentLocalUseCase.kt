package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.ARContentDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IARContentRepository

class SaveARContentLocalUseCase(
    private val arContentRepository: IARContentRepository
) {
    suspend operator fun invoke(
        arContent: ARContentDomainModel
    ): TResult<Unit, AppExceptionDomainModel> {
        return arContentRepository.saveARContentLocal(arContent)
    }
}