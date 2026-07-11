package com.pavlusha.data.repository

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.pavlusha.data.local.dao.UserDao
import com.pavlusha.data.mapper.UserDataMapper
import com.pavlusha.data.mapper.UserLocalDataMapper
import com.pavlusha.data.mapper.exception.toAppExceptionDomainModel
import com.pavlusha.data.mapper.remote.UserRemoteDataMapper
import com.pavlusha.data.remote.UserRemoteDataSource
import com.pavlusha.domain.TResult
import com.pavlusha.domain.model.UserDomainModel
import com.pavlusha.domain.model.exception.AppExceptionDomainModel
import com.pavlusha.domain.repository.IAuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userDao: UserDao
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
        val firebaseUser = firebaseAuth.currentUser ?: return null

        val localUser = userDao.getUserById(firebaseUser.uid)

        return if (localUser != null) {
            UserLocalDataMapper.toDomainFromData(localUser)
        } else {
            val domainUser = UserDataMapper.toDomainFromFirebase(firebaseUser)
            userDao.insertOrUpdateUser(UserLocalDataMapper.fromDomainToData(domainUser))
            domainUser
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): TResult<UserDomainModel, AppExceptionDomainModel> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User creation failed: null user")

            val domainUser = UserDataMapper.toDomainFromFirebase(user)

            val remoteUser = UserRemoteDataMapper.fromDomainToData(domainUser)
            userRemoteDataSource.saveUserProfile(remoteUser)

            val localUser = UserLocalDataMapper.fromDomainToData(domainUser)
            userDao.insertOrUpdateUser(localUser)

            TResult.Success(domainUser)
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
            val firebaseUser = result.user ?: throw Exception("Sign in failed: null user")

            val remoteProfile = userRemoteDataSource.getUserProfile(firebaseUser.uid)

            val domainUser = if (remoteProfile != null) {
                UserRemoteDataMapper.toDomainFromData(remoteProfile)
            } else {
                val fallbackUser = UserDataMapper.toDomainFromFirebase(firebaseUser)
                userRemoteDataSource.saveUserProfile(UserRemoteDataMapper.fromDomainToData(fallbackUser))
                fallbackUser
            }

            userDao.insertOrUpdateUser(UserLocalDataMapper.fromDomainToData(domainUser))
            TResult.Success(domainUser)
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
            userDao.clearUser()
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

    override suspend fun updateUserProfile(user: UserDomainModel): TResult<Unit, AppExceptionDomainModel> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
                ?: throw Exception("User not logged in")

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(user.displayName)
                .apply {
                    if (user.photoUrl != null) {
                        setPhotoUri(user.photoUrl!!.toUri())
                    }
                }
                .build()

            firebaseUser.updateProfile(profileUpdates).await()

            val remoteUser = UserRemoteDataMapper.fromDomainToData(user)
            userRemoteDataSource.saveUserProfile(remoteUser)

            val localUser = UserLocalDataMapper.fromDomainToData(user)
            userDao.insertOrUpdateUser(localUser)

            TResult.Success(Unit)
        } catch (e: Exception) {
            TResult.Error(e.toAppExceptionDomainModel())
        }
    }
}