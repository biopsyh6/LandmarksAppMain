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
        is AppExceptionDomainModel.AccessDenied -> R.string.error_access_denied
        is AppExceptionDomainModel.DownloadFailed -> R.string.error_download_failed
        is AppExceptionDomainModel.InsufficientStorage -> R.string.error_insufficient_storage
        is AppExceptionDomainModel.LocationDisabled -> R.string.error_location_disabled
        is AppExceptionDomainModel.ModelError -> R.string.error_model_failed
        is AppExceptionDomainModel.EmptySearchResult -> R.string.error_empty_search
        is AppExceptionDomainModel.SearchLimitExceeded -> R.string.error_search_limit
    }
}