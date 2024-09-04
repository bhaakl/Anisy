package com.bhaakl.anisy.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bhaakl.anisy.data.datasource.local.entity.CalEventText
import com.bhaakl.anisy.data.datasource.local.entity.User
import com.bhaakl.anisy.data.datasource.local.dao.CalEventTextQuery
import com.bhaakl.anisy.data.datasource.local.dao.UserQuery
import com.bhaakl.anisy.data.util.RoleConverter

@Database(entities = [CalEventText::class, User::class], version = 11, exportSchema = false)
@TypeConverters(RoleConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun calEventTextQuery(): CalEventTextQuery
    abstract fun userDao(): UserQuery
}