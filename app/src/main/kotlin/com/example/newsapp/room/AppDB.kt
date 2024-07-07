package com.example.newsapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.CalEventText
import com.example.newsapp.room.repo.CalEventTextQuery
import com.example.newsapp.room.repo.FavouriteQuery
import com.example.newsapp.utils.RoleConverter

@Database(entities = [FavouriteModel::class, CalEventText::class], version = 3, exportSchema = false)
@TypeConverters(RoleConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun favouriteQuery(): FavouriteQuery
    abstract fun calEventTextQuery(): CalEventTextQuery
}