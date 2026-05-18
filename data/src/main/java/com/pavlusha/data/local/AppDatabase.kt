package com.pavlusha.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pavlusha.data.local.dao.ARContentDao
import com.pavlusha.data.local.dao.GeoFenceDao
import com.pavlusha.data.local.dao.LandmarkDao
import com.pavlusha.data.local.dao.UserContentDao
import com.pavlusha.data.local.dao.UserDao
import com.pavlusha.data.local.entity.ARAnnotationEntity
import com.pavlusha.data.local.entity.ARContentConfigEntity
import com.pavlusha.data.local.entity.ExternalInfoEntity
import com.pavlusha.data.local.entity.GeoFenceEntity
import com.pavlusha.data.local.entity.HistoricalPeriodEntity
import com.pavlusha.data.local.entity.LandmarkEntity
import com.pavlusha.data.local.entity.LandmarkGalleryEntity
import com.pavlusha.data.local.entity.LandmarkSourceUrlEntity
import com.pavlusha.data.local.entity.LandmarkTagEntity
import com.pavlusha.data.local.entity.RegionPackageEntity
import com.pavlusha.data.local.entity.UserARPhotoEntity
import com.pavlusha.data.local.entity.UserEntity
import com.pavlusha.data.local.entity.UserNoteEntity
import com.pavlusha.data.local.entity.VisitHistoryEntity

@Database(
    entities = [
        LandmarkEntity::class,
        HistoricalPeriodEntity::class,
        LandmarkGalleryEntity::class,
        LandmarkTagEntity::class,
        LandmarkSourceUrlEntity::class,
        UserNoteEntity::class,
        VisitHistoryEntity::class,
        RegionPackageEntity::class,
        ExternalInfoEntity::class,
        ARAnnotationEntity::class,
        ARContentConfigEntity::class,
        GeoFenceEntity::class,
        UserEntity::class,
        UserARPhotoEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun landmarkDao(): LandmarkDao
    abstract fun userDao(): UserDao
    abstract fun userContentDao(): UserContentDao
    abstract fun arContentDao(): ARContentDao
    abstract fun geoFenceDao(): GeoFenceDao
}