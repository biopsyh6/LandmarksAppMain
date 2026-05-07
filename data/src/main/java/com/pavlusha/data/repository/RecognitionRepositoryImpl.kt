package com.pavlusha.data.repository

import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.remote.MLRecognitionDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RecognitionResultDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IRecognitionRepository

class RecognitionRepositoryImpl(
    private val recognitionDataSource: MLRecognitionDataSource
) : IRecognitionRepository {
    override suspend fun recognizeLandmark(
        imageBytes: ByteArray,
        rotation: Int
    ): TResult<RecognitionResultDomainModel?, AppExceptionDomainModel> {
        return try {
            val result = recognitionDataSource.recognizeImage(imageBytes, rotation)
            TResult.Success(result)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun updateMLModel(modelPath: String): TResult<Unit, AppExceptionDomainModel> {
        return try {
            recognitionDataSource.loadNewModel(modelPath)
            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }
}