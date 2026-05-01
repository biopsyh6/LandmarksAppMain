package com.pavlusha.landmarksapp.di

import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.ILandmarkSearchRepository
import com.pavlusha.domain.repository.ILocationRepository
import com.pavlusha.domain.usecase.GetCurrentUserUseCase
import com.pavlusha.domain.usecase.GetLandmarkDetailsUseCase
import com.pavlusha.domain.usecase.GetLocalLandmarkDetailsUseCase
import com.pavlusha.domain.usecase.GetNearbyLandmarksUseCase
import com.pavlusha.domain.usecase.GetUserLocationUseCase
import com.pavlusha.domain.usecase.ObserveUserLocationUseCase
import com.pavlusha.domain.usecase.ResetPasswordUseCase
import com.pavlusha.domain.usecase.SearchLandmarksAtPointUseCase
import com.pavlusha.domain.usecase.SearchLandmarksUseCase
import com.pavlusha.domain.usecase.SearchLocalLandmarksUseCase
import com.pavlusha.domain.usecase.SignInUseCase
import com.pavlusha.domain.usecase.SignInWithGoogleUseCase
import com.pavlusha.domain.usecase.SignOutUseCase
import com.pavlusha.domain.usecase.SignUpUseCase
import com.pavlusha.domain.usecase.ToggleFavoriteUseCase
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


    factory<GetUserLocationUseCase> {
        GetUserLocationUseCase(get<ILocationRepository>())
    }

    factory<ObserveUserLocationUseCase> {
        ObserveUserLocationUseCase(get<ILocationRepository>())
    }

    factory<GetLandmarkDetailsUseCase> {
        GetLandmarkDetailsUseCase(
            localRepository = get<ILandmarkRepository>(),
            searchRepository = get<ILandmarkSearchRepository>()
        )
    }

    factory<GetLocalLandmarkDetailsUseCase> {
        GetLocalLandmarkDetailsUseCase(
            repository = get<ILandmarkRepository>()
        )
    }

    factory<GetNearbyLandmarksUseCase> {
        GetNearbyLandmarksUseCase(
            repository = get<ILandmarkRepository>()
        )
    }

    factory<SearchLandmarksAtPointUseCase> {
        SearchLandmarksAtPointUseCase(
            searchRepository = get<ILandmarkSearchRepository>()
        )
    }

    factory<SearchLandmarksUseCase> {
        SearchLandmarksUseCase(
            searchRepository = get<ILandmarkSearchRepository>(),
            localRepository = get<ILandmarkRepository>()
        )
    }

    factory<SearchLocalLandmarksUseCase> {
        SearchLocalLandmarksUseCase(
            repository = get<ILandmarkRepository>()
        )
    }

    factory<ToggleFavoriteUseCase> {
        ToggleFavoriteUseCase(
            landmarkRepository = get<ILandmarkRepository>(),
            authRepository = get<IAuthRepository>()
        )
    }
}