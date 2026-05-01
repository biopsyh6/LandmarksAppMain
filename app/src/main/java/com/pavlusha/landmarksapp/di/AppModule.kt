package com.pavlusha.landmarksapp.di

import com.pavlusha.landmarksapp.ui.viewmodel.LandmarkDetailsViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.LoginViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.MapViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.RegisterViewModel
import com.pavlusha.landmarksapp.ui.viewmodel.ResetPasswordViewModel
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
            ioDispatcher = get()
        )
    }

    viewModel<LandmarkDetailsViewModel> {
        LandmarkDetailsViewModel(
            getLandmarkDetailsUseCase = get()
        )
    }
}