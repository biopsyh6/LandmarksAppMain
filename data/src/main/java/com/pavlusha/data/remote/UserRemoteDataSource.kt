package com.pavlusha.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.pavlusha.data.model.remote.UserNoteRemoteModel
import com.pavlusha.data.model.remote.UserRemoteModel
import com.pavlusha.data.model.remote.VisitHistoryRemoteModel
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource(
    private val firestore: FirebaseFirestore
) {
    private val usersCollection = firestore.collection("users")

    suspend fun getUserProfile(userId: String): UserRemoteModel? {
        return try {
            val snapshot = usersCollection.document(userId).get().await()
            if (snapshot.exists()) {
                snapshot.toObject(UserRemoteModel::class.java)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun saveUserProfile(user: UserRemoteModel) {
        try {
            usersCollection.document(user.id).set(user).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getUserNotes(userId: String): List<UserNoteRemoteModel> {
        return try {
            val snapshot = usersCollection.document(userId)
                .collection("notes")
                .get()
                .await()
            snapshot.toObjects(UserNoteRemoteModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun saveUserNote(userId: String, note: UserNoteRemoteModel) {
        try {
            usersCollection.document(userId)
                .collection("notes")
                .document(note.id)
                .set(note)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteUserNote(userId: String, noteId: String) {
        try {
            usersCollection.document(userId)
                .collection("notes")
                .document(noteId)
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getVisitHistory(userId: String): List<VisitHistoryRemoteModel> {
        return try {
            val snapshot = usersCollection.document(userId)
                .collection("visit_history")
                .get()
                .await()
            snapshot.toObjects(VisitHistoryRemoteModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun saveVisitHistory(userId: String, history: VisitHistoryRemoteModel) {
        try {
            usersCollection.document(userId)
                .collection("visit_history")
                .document(history.id)
                .set(history)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}