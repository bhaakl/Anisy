package com.bhaakl.newsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bhaakl.newsapp.models.CalEventText
import com.bhaakl.newsapp.models.User
import com.bhaakl.newsapp.data.dao.CalEventTextQuery
import com.bhaakl.newsapp.data.dao.UserQuery
import com.bhaakl.newsapp.utils.RoleConverter

@Database(entities = [CalEventText::class, User::class], version = 10, exportSchema = false)
@TypeConverters(RoleConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun calEventTextQuery(): CalEventTextQuery
    abstract fun userDao(): UserQuery
}