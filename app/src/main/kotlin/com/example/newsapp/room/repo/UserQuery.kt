package com.example.newsapp.room.repo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.newsapp.models.CalEventText
import com.example.newsapp.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserQuery {
    @Query("SELECT * FROM User")
    fun getAllUsers(): Flow<List<User>>

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User>

    @Query("SELECT * FROM User WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User?>

    @Query("UPDATE User SET is_liked = :isLiked WHERE email = :email")
    suspend fun updateIsLiked(email: String, isLiked: Boolean)

    @Query("SELECT id FROM User WHERE email = :email")
    suspend fun getUserIdByEmail(email: String): Int?
}