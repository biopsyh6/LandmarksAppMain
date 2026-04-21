package com.pavlusha.landmarksapp.util

import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.landmarksapp.R

fun AppExceptionDomainModel.parseToResource(): Int {
    return when(this) {
        is AppExceptionDomainModel.NoInternet -> R.string.error_no_internet
        is AppExceptionDomainModel.NoAuth -> R.string.error_no_auth
        is AppExceptionDomainModel.NotFound -> R.string.error_not_found
        is AppExceptionDomainModel.ServerError -> R.string.error_server
        is AppExceptionDomainModel.WeakPassword -> R.string.error_weak_password
        is AppExceptionDomainModel.UserAlreadyExists -> R.string.error_user_exists
        is AppExceptionDomainModel.InvalidCredentials -> R.string.error_invalid_credentials
        is AppExceptionDomainModel.Other -> R.string.error_unknown
        is AppExceptionDomainModel.InvalidEmail -> R.string.error_invalid_email
    }
}