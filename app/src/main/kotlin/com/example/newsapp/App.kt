package com.example.newsapp

import android.app.Application
import androidx.room.Room
import com.example.newsapp.room.AppDB

class App : Application() {

    lateinit var database: AppDB

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDB::class.java, "newsapp_db")
            .fallbackToDestructiveMigration()
            .build()
    }
}