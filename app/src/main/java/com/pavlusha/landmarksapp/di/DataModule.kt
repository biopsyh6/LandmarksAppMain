package com.pavlusha.landmarksapp.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pavlusha.data.local.AppDatabase
import com.pavlusha.data.remote.LandmarkRemoteDataSource
import com.pavlusha.data.remote.LocalPhotoStorageDataSource
import com.pavlusha.data.remote.MLRecognitionDataSource
import com.pavlusha.data.remote.UserRemoteDataSource
import com.pavlusha.data.remote.WikipediaDataSource
import com.pavlusha.data.remote.YandexLocationDataSource
import com.pavlusha.data.remote.YandexRoutingDataSource
import com.pavlusha.data.remote.YandexSearchDataSource
import com.pavlusha.data.remote.api.IWikipediaApi
import com.pavlusha.data.repository.ARContentRepositoryImpl
import com.pavlusha.data.repository.AuthRepositoryImpl
import com.pavlusha.data.repository.CompassRepositoryImpl
import com.pavlusha.data.repository.LandmarkRepositoryImpl
import com.pavlusha.data.repository.LandmarkSearchRepositoryImpl
import com.pavlusha.data.repository.LocationRepositoryImpl
import com.pavlusha.data.repository.RecognitionRepositoryImpl
import com.pavlusha.data.repository.RoutingRepositoryImpl
import com.pavlusha.data.repository.UserContentRepositoryImpl
import com.pavlusha.data.sensor.DeviceCompass
import com.pavlusha.domain.repository.IARContentRepository
import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ICompassRepository
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.ILandmarkSearchRepository
import com.pavlusha.domain.repository.ILocationRepository
import com.pavlusha.domain.repository.IRecognitionRepository
import com.pavlusha.domain.repository.IRoutingRepository
import com.pavlusha.domain.repository.IUserContentRepository
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.transport.TransportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

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

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://ru.wikipedia.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<IWikipediaApi> {
        get<Retrofit>().create(IWikipediaApi::class.java)
    }
    single {
        WikipediaDataSource(apiService = get())
    }

    single { get<AppDatabase>().landmarkDao() }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().userContentDao() }
    single { get<AppDatabase>().arContentDao() }
    single { get<AppDatabase>().geoFenceDao() }


    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    single<IAuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get(),
            userRemoteDataSource = get(),
            userDao = get()
        )
    }

    single<ILocationRepository> {
        LocationRepositoryImpl(
            locationDataSource = get()
        )
    }

    single<ILandmarkRepository> {
        LandmarkRepositoryImpl(
            landmarkDao = get(),
            remoteDataSource = get()
        )
    }

    single<ILandmarkSearchRepository> {
        LandmarkSearchRepositoryImpl(
            searchDataSource = get(),
            wikipediaDataSource = get(),
            landmarkDao = get()
        )
    }

    single<IRecognitionRepository> {
        RecognitionRepositoryImpl(
            recognitionDataSource = get()
        )
    }

    single<IUserContentRepository> {
        UserContentRepositoryImpl(
            userContentDao = get(),
            userRemoteDataSource = get(),
            photoStorageDataSource = get()
        )
    }

    single<IARContentRepository> {
        ARContentRepositoryImpl(
            arContentDao = get(),
            remoteDataSource = get()
        )
    }

    single<IRoutingRepository> {
        RoutingRepositoryImpl(
            routingDataSource = get()
        )
    }

    single<ICompassRepository> {
        CompassRepositoryImpl(
            deviceCompass = get()
        )
    }

    single {
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    }

    single {
        MapKitFactory.getInstance().createLocationManager()
    }

    single {
        TransportFactory.getInstance().createPedestrianRouter()
    }

    single {
        LandmarkRemoteDataSource(firestore = get())
    }

    single {
        UserRemoteDataSource(firestore = get())
    }

    single {
        YandexSearchDataSource(searchManager = get())
    }

    single {
        YandexLocationDataSource(locationManager = get())
    }

    single {
        MLRecognitionDataSource(context = androidContext())
    }

    single {
        LocalPhotoStorageDataSource(context = androidContext())
    }

    single {
        YandexRoutingDataSource(pedestrianRouter = get())
    }

    single {
        DeviceCompass(context = androidContext())
    }
}