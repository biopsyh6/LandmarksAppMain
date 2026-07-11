package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import kotlinx.coroutines.flow.Flow

interface IRegionRepository {
    suspend fun getAvailableRegions(): TResult<List<RegionPackageDomainModel>, AppExceptionDomainModel>

    suspend fun deleteRegion(regionId: String): TResult<Unit, AppExceptionDomainModel>

    fun downloadRegion(regionId: String): Flow<TResult<Float, AppExceptionDomainModel>>

    suspend fun getDownloadedRegions(): TResult<List<RegionPackageDomainModel>, AppExceptionDomainModel>
}