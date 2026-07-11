package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RecognitionResultDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel

interface IRecognitionRepository {
    suspend fun recognizeLandmark(
        imageBytes: ByteArray,
        rotation: Int
    ): TResult<RecognitionResultDomainModel?, AppExceptionDomainModel>

    suspend fun updateMLModel(
        modelPath: String
    ): TResult<Unit, AppExceptionDomainModel>
}