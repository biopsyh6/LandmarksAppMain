package com.pavlusha.domain.repository

import kotlinx.coroutines.flow.Flow

interface ICompassRepository {
    fun observeHeading(): Flow<Float>
}