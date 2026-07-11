package com.pavlusha.data.repository

import com.pavlusha.data.local.dao.ARContentDao
import com.pavlusha.data.mapper.ARContentLocalDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.mapper.remote.ARContentRemoteDataMapper
import com.pavlusha.data.remote.LandmarkRemoteDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.ARContentDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IARContentRepository

class ARContentRepositoryImpl(
    private val arContentDao: ARContentDao,
    private val remoteDataSource: LandmarkRemoteDataSource
) : IARContentRepository {
    override suspend fun getARContentForLandmark(landmarkId: String): TResult<ARContentDomainModel, AppExceptionDomainModel> {
        return try {
            val remoteConfig = remoteDataSource.getARConfig(landmarkId)

            if (remoteConfig != null) {
                val domainModel = ARContentRemoteDataMapper.toDomainFromData(remoteConfig)

                val localDataDetails = ARContentLocalDataMapper.fromDomainToData(domainModel)

                arContentDao.insertFullARContent(
                    config = localDataDetails.config,
                    annotations = localDataDetails.annotations
                )
            }

            val localData = arContentDao.getARContentByLandmarkId(landmarkId)

            if (localData != null) {
                val finalDomainModel = ARContentLocalDataMapper.toDomainFromData(localData)
                TResult.Success(finalDomainModel)
            } else {
                TResult.Error(
                    AppExceptionDomainModel.NotFound(Exception("AR Config for landmark $landmarkId not found"))
                )
            }
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun saveARContentLocal(arContent: ARContentDomainModel): TResult<Unit, AppExceptionDomainModel> {
        return try {
            val localDataDetails = ARContentLocalDataMapper.fromDomainToData(arContent)

            arContentDao.insertFullARContent(
                config = localDataDetails.config,
                annotations = localDataDetails.annotations
            )

            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }
}