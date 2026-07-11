package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.LandmarkDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.IRecognitionRepository

class IdentifyLandmarkUseCase(
    private val recognitionRepository: IRecognitionRepository,
    private val landmarkRepository: ILandmarkRepository
) {
    suspend operator fun invoke(
        imageBytes: ByteArray,
        rotation: Int
    ): TResult<LandmarkDomainModel, AppExceptionDomainModel> {
        val recognitionResult = recognitionRepository.recognizeLandmark(imageBytes, rotation)

        return when (recognitionResult) {
            is TResult.Success -> {
                val resultData = recognitionResult.data
                if (resultData == null) {
                    return TResult.Error(AppExceptionDomainModel.Other(Exception("Object not recognized")))
                }
                landmarkRepository.getLandmarkByRecognition(resultData)
            }
            is TResult.Error -> TResult.Error(recognitionResult.exception)
        }
    }
}