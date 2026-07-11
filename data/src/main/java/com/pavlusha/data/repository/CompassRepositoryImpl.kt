package com.pavlusha.data.repository

import com.pavlusha.data.sensor.DeviceCompass
import com.pavlusha.domain.repository.ICompassRepository
import kotlinx.coroutines.flow.Flow

class CompassRepositoryImpl(
    private val deviceCompass: DeviceCompass
) : ICompassRepository {
    override fun observeHeading(): Flow<Float> {
        return deviceCompass.observeHeading()
    }
}