package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RecognitionResultDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IRecognitionRepository

class RecognizeLandmarkUseCase(
    private val repository: IRecognitionRepository
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        rotation: Int
    ): TResult<RecognitionResultDomainModel?, AppExceptionDomainModel> =
        repository.recognizeLandmark(imageBytes, rotation)
}