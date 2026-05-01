package com.pavlusha.landmarksapp.di.app

import android.app.Application
import com.pavlusha.landmarksapp.BuildConfig
import com.pavlusha.landmarksapp.di.appModule
import com.pavlusha.landmarksapp.di.dataModule
import com.pavlusha.landmarksapp.di.domainModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }
    }
}