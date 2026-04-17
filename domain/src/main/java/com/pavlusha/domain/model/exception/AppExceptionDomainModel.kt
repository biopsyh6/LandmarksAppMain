package com.pavlusha.domain.model.exception

sealed class AppExceptionDomainModel(exception: Throwable) : Throwable(exception) {
    override val cause: Throwable = exception

    data class NoInternet(val e: Throwable) : AppExceptionDomainModel(e)
    data class NoAuth(val e: Throwable) : AppExceptionDomainModel(e)
    data class NotFound(val e: Throwable) : AppExceptionDomainModel(e)
    data class ServerError(val e: Throwable) : AppExceptionDomainModel(e)
    data class Other(val e: Throwable) : AppExceptionDomainModel(e)
    data class WeakPassword(val e: Throwable) : AppExceptionDomainModel(e)
    data class UserAlreadyExists(val e: Throwable) : AppExceptionDomainModel(e)
    data class InvalidCredentials(val e: Throwable) : AppExceptionDomainModel(e)
}