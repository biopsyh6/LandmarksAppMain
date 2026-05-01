package com.pavlusha.domain.model.exception

sealed class AppExceptionDomainModel(exception: Throwable) : Throwable(exception) {
    override val cause: Throwable = exception

    // общие
    data class NoInternet(val e: Throwable) : AppExceptionDomainModel(e)
    data class NotFound(val e: Throwable) : AppExceptionDomainModel(e)
    data class ServerError(val e: Throwable) : AppExceptionDomainModel(e)
    data class Other(val e: Throwable) : AppExceptionDomainModel(e)

    // авторизация
    data class NoAuth(val e: Throwable) : AppExceptionDomainModel(e)
    data class WeakPassword(val e: Throwable) : AppExceptionDomainModel(e)
    data class UserAlreadyExists(val e: Throwable) : AppExceptionDomainModel(e)
    data class InvalidCredentials(val e: Throwable) : AppExceptionDomainModel(e)
    data class InvalidEmail(val e: Throwable) : AppExceptionDomainModel(e)

    // разрешения
    data class AccessDenied(val e: Throwable) : AppExceptionDomainModel(e)
    data class LocationDisabled(val e: Throwable) : AppExceptionDomainModel(e)

    // оффлайн
    data class InsufficientStorage(val e: Throwable) : AppExceptionDomainModel(e)
    data class DownloadFailed(val e: Throwable) : AppExceptionDomainModel(e)

    // ML
    data class ModelError(val e: Throwable) : AppExceptionDomainModel(e)

    // поиск и API
    data class SearchLimitExceeded(val e: Throwable) : AppExceptionDomainModel(e)
    data class EmptySearchResult(val e: Throwable) : AppExceptionDomainModel(e)
}