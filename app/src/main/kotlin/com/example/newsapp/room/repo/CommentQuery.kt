package com.example.newsapp.room.repo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newsapp.models.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentQuery {
    @Insert
    suspend fun insert(comment: Comment): Long

    @Query("SELECT * FROM Comment ORDER BY created_at DESC")
    fun getAll(): Flow<List<Comment>>

    @Query("SELECT COUNT(*) FROM Comment WHERE id < :commentId")
    suspend fun getPositionById(commentId: Long): Int

    @Query("DELETE FROM Comment")
    suspend fun deleteAll()
}