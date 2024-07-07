package com.example.newsapp.room.repo

import androidx.room.Dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.room.FavouriteModel

@Dao
interface FavouriteQuery {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(favouriteModel: FavouriteModel)

    @Query("SELECT * FROM FavouriteModel WHERE title = :title LIMIT 1")
    suspend fun getFavouriteByTitle(title: String): FavouriteModel?

    @Query("SELECT * FROM FavouriteModel ORDER BY id DESC")
    fun getAllFavourites(): List<FavouriteModel>

    @Query("SELECT EXISTS(SELECT * FROM FavouriteModel WHERE title = :title)")
    suspend fun isExists(title: String): Boolean

    @Delete
    suspend fun delete(favouriteModel: FavouriteModel)

}