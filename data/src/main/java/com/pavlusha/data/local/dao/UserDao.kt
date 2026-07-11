package com.pavlusha.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pavlusha.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserIgnore(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Transaction
    suspend fun insertOrUpdateUser(user: UserEntity) {
        val id = insertUserIgnore(user)
        if (id == -1L) {
            updateUser(user)
        }
    }

    @Query("DELETE FROM users")
    suspend fun clearUser()
}