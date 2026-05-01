package com.pavlusha.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.pavlusha.data.mapper.UserDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : IAuthRepository {
    override fun observeAuthState(): Flow<UserDomainModel?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val userDomain = auth.currentUser?.let {
                UserDataMapper.toDomainFromFirebase(it)
            }

            trySend(userDomain)
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }


    override suspend fun getCurrentUser(): UserDomainModel? {
        return firebaseAuth.currentUser?.let {
            UserDataMapper.toDomainFromFirebase(it)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): TResult<UserDomainModel, AppExceptionDomainModel> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User creation failed: null user")
            TResult.Success(UserDataMapper.toDomainFromFirebase(user))
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): TResult<UserDomainModel, AppExceptionDomainModel> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("Sign in failed: null user")
            TResult.Success(UserDataMapper.toDomainFromFirebase(user))
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun signInWithGoogle(idToken: String): TResult<UserDomainModel, AppExceptionDomainModel> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut(): TResult<Unit, AppExceptionDomainModel> {
        return try {
            firebaseAuth.signOut()
            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }

    override suspend fun resetPassword(email: String): TResult<Unit, AppExceptionDomainModel> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }
}