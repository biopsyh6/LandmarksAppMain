package com.pavlusha.data.mapper.exception

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.NotFoundError
import com.yandex.runtime.network.RemoteError
import java.net.ConnectException
import java.net.UnknownHostException

fun Throwable.toAppExceptionDomainModel(): AppExceptionDomainModel {
    return when(this) {
        is UnknownHostException,
        is ConnectException,
        is FirebaseNetworkException -> AppExceptionDomainModel.NoInternet(this)
        is FirebaseAuthWeakPasswordException -> {
            AppExceptionDomainModel.WeakPassword(this)
        }
        is FirebaseAuthInvalidCredentialsException -> {
            AppExceptionDomainModel.InvalidCredentials(this)
        }
        is FirebaseAuthInvalidUserException -> {
            AppExceptionDomainModel.NoAuth(this)
        }
        is FirebaseAuthUserCollisionException -> {
            AppExceptionDomainModel.UserAlreadyExists(this)
        }
        is AppExceptionDomainModel -> this
        else -> AppExceptionDomainModel.Other(this)
    }
}

fun Error.toAppExceptionDomainModel(): AppExceptionDomainModel {
    return when(this) {
        is NetworkError -> {
            AppExceptionDomainModel.NoInternet(Exception("Yandex Maps Network Error"))
        }
        is RemoteError -> {
            AppExceptionDomainModel.ServerError(Exception("Yandex Maps Server Error"))
        }
        is NotFoundError -> {
            AppExceptionDomainModel.EmptySearchResult(Exception("Objects not founded"))
        }
        else -> {
            AppExceptionDomainModel.Other(Exception("MapKit error: ${this.javaClass.simpleName}"))
        }
    }
}