package com.pavlusha.landmarksapp.di

import com.pavlusha.landmarksapp.ui.viewmodel.ARGalleryViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.FavoritesViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.LandmarkDetailsViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.LoginViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.MapViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.MyNotesViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.ProfileViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.RecognitionViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.RegisterViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.ResetPasswordViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.VisitHistoryViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Dispatchers.IO }
    viewModel<LoginViewModel> {
        LoginViewModel(
            signInUseCase = get(),
            signInWithGoogleUseCase = get(),
            ioDispatcher = get()
        )
    }
    viewModel<RegisterViewModel> {
        RegisterViewModel(
            signUpUseCase = get(),
            ioDispatcher = get()
        )
    }
    viewModel<ResetPasswordViewModel> {
        ResetPasswordViewModel(
            resetPasswordUseCase = get(),
            ioDispatcher = get()
        )
    }
    viewModel<MapViewModel> {
        MapViewModel(
            observeUserLocationUseCase = get(),
            getUserLocationUseCase = get(),
            searchLandmarksUseCase = get(),
            searchLandmarksAtPointUseCase = get(),
            ioDispatcher = get(),
            getPedestrianRouteUseCase = get(),
            addToHistoryUseCase = get()
        )
    }

    viewModel<LandmarkDetailsViewModel> {
        LandmarkDetailsViewModel(
            getLandmarkDetailsUseCase = get(),
            getWikipediaInfoUseCase = get(),
            toggleFavoriteUseCase = get(),
            saveLandmarkNoteUseCase = get()
        )
    }

    viewModel<RecognitionViewModel> {
        RecognitionViewModel(
            identifyLandmarkUseCase = get(),
            getARContentForLandmarkUseCase = get(),
            saveARPhotoUseCase = get(),
            observeUserLocationUseCase = get(),
            observeCompassHeadingUseCase = get()
        )
    }

    viewModel<ProfileViewModel> {
        ProfileViewModel(
            getCurrentUserUseCase = get(),
            signOutUseCase = get(),
            updateUserProfileUseCase = get()
        )
    }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(
            getFavoriteLandmarksUseCase = get(),
            toggleFavoriteUseCase = get(),
            getWikipediaInfoUseCase = get()
        )
    }

    viewModel<ARGalleryViewModel> {
        ARGalleryViewModel(
            getUserGalleryUseCase = get(),
            deleteARPhotoUseCase = get()
        )
    }

    viewModel<MyNotesViewModel> {
        MyNotesViewModel(
            getAllNotesWithLandmarksUseCase = get(),
            deleteNoteUseCase = get(),
            updateNoteUseCase = get()
        )
    }

    viewModel<VisitHistoryViewModel> {
        VisitHistoryViewModel(
            getVisitHistoryWithLandmarksUseCase = get(),
            getWikipediaInfoUseCase = get()
        )
    }
}