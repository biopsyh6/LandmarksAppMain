package com.pavlusha.landmarksapp.di

import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.usecase.GetCurrentUserUseCase
import com.pavlusha.domain.usecase.ResetPasswordUseCase
import com.pavlusha.domain.usecase.SignInUseCase
import com.pavlusha.domain.usecase.SignInWithGoogleUseCase
import com.pavlusha.domain.usecase.SignOutUseCase
import com.pavlusha.domain.usecase.SignUpUseCase
import org.koin.dsl.module

val domainModule = module {
    factory<GetCurrentUserUseCase> {
        GetCurrentUserUseCase(get<IAuthRepository>())
    }

    factory<SignUpUseCase> {
        SignUpUseCase(get<IAuthRepository>())
    }

    factory<SignInUseCase> {
        SignInUseCase(get<IAuthRepository>())
    }

    factory<SignInWithGoogleUseCase> {
        SignInWithGoogleUseCase(get<IAuthRepository>())
    }

    factory<SignOutUseCase> {
        SignOutUseCase(get<IAuthRepository>())
    }

    factory<ResetPasswordUseCase> {
        ResetPasswordUseCase(get<IAuthRepository>())
    }

//    factory<GetNearbyLandmarksUseCase> {
//        GetNearbyLandmarksUseCase(get<ILandmarkRepository>())
//    }
//
//    factory<GetLandmarkDetailsUseCase> {
//        GetLandmarkDetailsUseCase(get<ILandmarkRepository>())
//    }

}