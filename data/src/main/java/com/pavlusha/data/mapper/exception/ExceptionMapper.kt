package com.pavlusha.data.mapper.exception

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
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