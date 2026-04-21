package com.pavlusha.landmarksapp.di

import com.google.firebase.auth.FirebaseAuth
import com.pavlusha.data.repository.AuthRepositoryImpl
import com.pavlusha.domain.repository.IAuthRepository
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }

    single<IAuthRepository> {
        AuthRepositoryImpl(
            firebaseAuth = get()
        )
    }
}