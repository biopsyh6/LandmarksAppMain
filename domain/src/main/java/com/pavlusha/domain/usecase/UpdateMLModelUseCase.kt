package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IRecognitionRepository

class UpdateMLModelUseCase(
    private val repository: IRecognitionRepository
) {
    suspend operator fun invoke(
        modelPath: String
    ): TResult<Unit, AppExceptionDomainModel> {
        if (modelPath.isBlank()) {
            return TResult.Error(
                AppExceptionDomainModel.Other(Exception("Model path is empty"))
            )
        }

        if (!modelPath.endsWith(".tflite")) {
            return TResult.Error(
                AppExceptionDomainModel.Other(Exception("Invalid model format. Expected .tflite"))
            )
        }

        return repository.updateMLModel(modelPath)
    }
}