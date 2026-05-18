package com.pavlusha.landmarksapp.di

import com.pavlusha.domain.repository.IAuthRepository
import com.pavlusha.domain.repository.ILandmarkRepository
import com.pavlusha.domain.repository.ILandmarkSearchRepository
import com.pavlusha.domain.repository.ILocationRepository
import com.pavlusha.domain.usecase.AddToHistoryUseCase
import com.pavlusha.domain.usecase.DeleteARPhotoUseCase
import com.pavlusha.domain.usecase.GetARContentForLandmarkUseCase
import com.pavlusha.domain.usecase.GetCurrentUserUseCase
import com.pavlusha.domain.usecase.GetLandmarkDetailsUseCase
import com.pavlusha.domain.usecase.GetLocalLandmarkDetailsUseCase
import com.pavlusha.domain.usecase.GetNearbyLandmarksUseCase
import com.pavlusha.domain.usecase.GetNotesForLandmarkUseCase
import com.pavlusha.domain.usecase.GetPedestrianRouteUseCase
import com.pavlusha.domain.usecase.GetUserGalleryUseCase
import com.pavlusha.domain.usecase.GetUserLocationUseCase
import com.pavlusha.domain.usecase.GetWikipediaInfoUseCase
import com.pavlusha.domain.usecase.IdentifyLandmarkUseCase
import com.pavlusha.domain.usecase.ObserveAuthStateUseCase
import com.pavlusha.domain.usecase.ObserveUserLocationUseCase
import com.pavlusha.domain.usecase.RecognizeLandmarkUseCase
import com.pavlusha.domain.usecase.ResetPasswordUseCase
import com.pavlusha.domain.usecase.SaveARContentLocalUseCase
import com.pavlusha.domain.usecase.SaveARPhotoUseCase
import com.pavlusha.domain.usecase.SaveLandmarkNoteUseCase
import com.pavlusha.domain.usecase.SearchLandmarksAtPointUseCase
import com.pavlusha.domain.usecase.SearchLandmarksUseCase
import com.pavlusha.domain.usecase.SearchLocalLandmarksUseCase
import com.pavlusha.domain.usecase.SignInUseCase
import com.pavlusha.domain.usecase.SignInWithGoogleUseCase
import com.pavlusha.domain.usecase.SignOutUseCase
import com.pavlusha.domain.usecase.SignUpUseCase
import com.pavlusha.domain.usecase.ToggleFavoriteUseCase
import com.pavlusha.domain.usecase.UpdateMLModelUseCase
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

    factory<AddToHistoryUseCase> {
        AddToHistoryUseCase(
            userContentRepository = get(),
            authRepository = get()
        )
    }

    factory<DeleteARPhotoUseCase> {
        DeleteARPhotoUseCase(
            repository = get()
        )
    }

    factory<GetARContentForLandmarkUseCase> {
        GetARContentForLandmarkUseCase(
            arContentRepository = get()
        )
    }

    factory<GetNotesForLandmarkUseCase> {
        GetNotesForLandmarkUseCase(
            repository = get(),
            authRepository = get()
        )
    }

    factory<GetUserGalleryUseCase> {
        GetUserGalleryUseCase(
            repository = get(),
            authRepository = get()
        )
    }

    factory<IdentifyLandmarkUseCase> {
        IdentifyLandmarkUseCase(
            recognitionRepository = get(),
            landmarkRepository = get()
        )
    }

    factory<ObserveAuthStateUseCase> {
        ObserveAuthStateUseCase(
            repository = get()
        )
    }

    factory<RecognizeLandmarkUseCase> {
        RecognizeLandmarkUseCase(
            repository = get()
        )
    }

    factory<SaveARContentLocalUseCase> {
        SaveARContentLocalUseCase(
            arContentRepository = get()
        )
    }

    factory<SaveARPhotoUseCase> {
        SaveARPhotoUseCase(
            repository = get(),
            authRepository = get()
        )
    }

    factory<SaveLandmarkNoteUseCase> {
        SaveLandmarkNoteUseCase(
            userContentRepository = get(),
            authRepository = get()
        )
    }

    factory<UpdateMLModelUseCase> {
        UpdateMLModelUseCase(
            repository = get()
        )
    }

    factory<GetWikipediaInfoUseCase> {
        GetWikipediaInfoUseCase(
            searchRepository = get()
        )
    }

    factory<GetPedestrianRouteUseCase> {
        GetPedestrianRouteUseCase(
            routingRepository = get()
        )
    }
}