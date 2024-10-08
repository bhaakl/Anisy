package com.bhaakl.anisy.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bhaakl.anisy.data.datasource.local.entity.CalEventText
import kotlinx.coroutines.flow.Flow

@Dao
interface CalEventTextQuery {
    @Insert
    suspend fun insert(event: CalEventText): Long

    @Query("SELECT * FROM CalEventText ORDER BY created_at DESC")
    fun getAll(): Flow<List<CalEventText>>

    @Query("SELECT COUNT(*) FROM CalEventText WHERE id < :eventId")
    suspend fun getPositionById(eventId: Long): Int

    @Query("DELETE FROM CalEventText")
    suspend fun deleteAll()
}