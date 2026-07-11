package com.pavlusha.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.pavlusha.data.model.UserDataModel
import com.pavlusha.domain.model.UserDomainModel

object UserDataMapper {
    fun fromFirebaseToData(firebaseUser: FirebaseUser): UserDataModel = UserDataModel(
        uid = firebaseUser.uid,
        email = firebaseUser.email,
        displayName = firebaseUser.displayName,
        photoUrl = firebaseUser.photoUrl?.toString(),
        isAnonymous = firebaseUser.isAnonymous
    )

    fun toDomainModelFromData(data: UserDataModel): UserDomainModel = UserDomainModel(
        id = data.uid,
        email = data.email,
        displayName = data.displayName,
        photoUrl = data.photoUrl,
        isAnonymous = data.isAnonymous
    )

    fun toDomainFromFirebase(firebaseUser: FirebaseUser): UserDomainModel =
        toDomainModelFromData(fromFirebaseToData(firebaseUser))
}