package com.bhaakl.newsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bhaakl.newsapp.domain.model.CalEventText
import com.bhaakl.newsapp.domain.model.User
import com.bhaakl.newsapp.data.dao.CalEventTextQuery
import com.bhaakl.newsapp.data.dao.UserQuery
import com.bhaakl.newsapp.data.util.RoleConverter

@Database(entities = [CalEventText::class, User::class], version = 11, exportSchema = false)
@TypeConverters(RoleConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun calEventTextQuery(): CalEventTextQuery
    abstract fun userDao(): UserQuery
}