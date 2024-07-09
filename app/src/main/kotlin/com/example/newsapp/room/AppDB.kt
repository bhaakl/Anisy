package com.example.newsapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.models.CalEventText
import com.example.newsapp.models.Comment
import com.example.newsapp.models.User
import com.example.newsapp.room.repo.CalEventTextQuery
import com.example.newsapp.room.repo.CommentQuery
import com.example.newsapp.room.repo.UserQuery
import com.example.newsapp.utils.RoleConverter

@Database(entities = [CalEventText::class, User::class, Comment::class], version = 9, exportSchema = false)
@TypeConverters(RoleConverter::class)
abstract class AppDB : RoomDatabase() {
    abstract fun calEventTextQuery(): CalEventTextQuery
    abstract fun userDao(): UserQuery
    abstract fun commentQuery(): CommentQuery
}