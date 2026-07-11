package com.pavlusha.domain.usecase

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.VisitWithLandmarkDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.IUserContentRepository

class GetVisitHistoryWithLandmarksUseCase(
    private val userContentRepository: IUserContentRepository,
    private val landmarkRepository: ILandmarkRepository,
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): TResult<List<VisitWithLandmarkDomainModel>, AppExceptionDomainModel> {
        val user = authRepository.getCurrentUser()
            ?:return TResult.Error(AppExceptionDomainModel.NoAuth(Exception("User not logged in")))

        val visitsResult = userContentRepository.getUserVisitHistory(user.id)
        if (visitsResult !is TResult.Success) {
            return TResult.Error(AppExceptionDomainModel.Other(Exception("Ошибка при получении истории посещений")))
        }

        val enrichedVisits = mutableListOf<VisitWithLandmarkDomainModel>()

        for (visit in visitsResult.data) {
            val landmarkResult = landmarkRepository.getLandmarkById(visit.landmarkId)
            if (landmarkResult is TResult.Success) {
                enrichedVisits.add(VisitWithLandmarkDomainModel(visit, landmarkResult.data))
            }
        }

        return TResult.Success(enrichedVisits)
    }
}