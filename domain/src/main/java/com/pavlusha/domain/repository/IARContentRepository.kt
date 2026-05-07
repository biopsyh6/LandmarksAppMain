package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.ARContentDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel

interface IARContentRepository {
    suspend fun getARContentForLandmark(
        landmarkId: String
    ): TResult<ARContentDomainModel, AppExceptionDomainModel>

    suspend fun saveARContentLocal(
        arContent: ARContentDomainModel
    ): TResult<Unit, AppExceptionDomainModel>
}