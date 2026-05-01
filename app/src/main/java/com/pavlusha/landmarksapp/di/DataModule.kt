package com.pavlusha.landmarksapp.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.pavlusha.data.local.AppDatabase
import com.pavlusha.data.remote.YandexSearchDataSource
import com.pavlusha.data.repository.AuthRepositoryImpl
import com.pavlusha.data.repository.LandmarkRepositoryImpl
import com.pavlusha.data.repository.LandmarkSearchRepositoryImpl
import com.pavlusha.data.repository.LocationRepositoryImpl
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.ILandmarkSearchRepository
import com.pavlusha.domain.repository.ILocationRepository
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "landmark_ar_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    single { get<AppDatabase>().landmarkDao() }

    single { FirebaseAuth.getInstance() }

    single<IAuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get()
        )
    }

    single<ILocationRepository> {
        LocationRepositoryImpl()
    }

    single<ILandmarkRepository> {
        LandmarkRepositoryImpl(
            landmarkDao = get()
        )
    }

    single {
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    }

    single {
        YandexSearchDataSource(searchManager = get())
    }

    single<ILandmarkSearchRepository> {
        LandmarkSearchRepositoryImpl(
            searchDataSource = get(),
            searchManager = get()
        )
    }
}