package com.example.ecommapp.data.local.dao

import androidx.room.*
import com.example.ecommapp.data.local.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User?>
}