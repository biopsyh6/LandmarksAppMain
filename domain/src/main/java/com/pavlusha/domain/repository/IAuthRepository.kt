package com.pavlusha.domain.repository

import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    fun observeAuthState(): Flow<UserDomainModel?>

    suspend fun getCurrentUser(): UserDomainModel?

    suspend fun signUp(email: String, password: String): TResult<UserDomainModel, AppExceptionDomainModel>

    suspend fun signIn(email: String, password: String): TResult<UserDomainModel, AppExceptionDomainModel>

    suspend fun signInWithGoogle(idToken: String): TResult<UserDomainModel, AppExceptionDomainModel>

    suspend fun signOut(): TResult<Unit, AppExceptionDomainModel>

    suspend fun resetPassword(email: String): TResult<Unit, AppExceptionDomainModel>
}