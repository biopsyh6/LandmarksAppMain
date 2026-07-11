package com.pavlusha.data.mapper.remote

import com.pavlusha.data.model.remote.RegionPackageRemoteModel
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.RegionStatus

object OfflineRegionRemoteDataMapper {
    fun toDomainFromData(remote: RegionPackageRemoteModel): RegionPackageDomainModel {
        return RegionPackageDomainModel(
            regionId = remote.regionId,
            regionName = remote.regionName,
            sizeMb = remote.sizeMb,
            status = RegionStatus.AVAILABLE,
            progress = 0f,
            lastUpdated = remote.lastUpdated
        )
    }

    fun fromDomainToData(domain: RegionPackageDomainModel, downloadUrl: String = ""): RegionPackageRemoteModel {
        return RegionPackageRemoteModel(
            regionId = domain.regionId,
            regionName = domain.regionName,
            sizeMb = domain.sizeMb,
            downloadUrl = downloadUrl,
            lastUpdated = domain.lastUpdated ?: System.currentTimeMillis()
        )
    }
}