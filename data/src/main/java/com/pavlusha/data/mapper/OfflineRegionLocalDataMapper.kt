package com.pavlusha.data.mapper

import com.pavlusha.data.local.entity.RegionPackageEntity
import com.pavlusha.domain.model.RegionPackageDomainModel
import com.pavlusha.domain.model.RegionStatus

object OfflineRegionLocalDataMapper {
    fun toDomainFromData(entity: RegionPackageEntity): RegionPackageDomainModel {
        return RegionPackageDomainModel(
            regionId = entity.regionId,
            regionName = entity.regionName,
            sizeMb = entity.sizeMb,
            status = RegionStatus.valueOf(entity.status),
            progress = entity.progress,
            lastUpdated = entity.lastUpdated
        )
    }

    fun fromDomainToData(domain: RegionPackageDomainModel): RegionPackageEntity {
        return RegionPackageEntity(
            regionId = domain.regionId,
            regionName = domain.regionName,
            sizeMb = domain.sizeMb,
            status = domain.status.name,
            progress = domain.progress,
            lastUpdated = domain.lastUpdated
        )
    }
}