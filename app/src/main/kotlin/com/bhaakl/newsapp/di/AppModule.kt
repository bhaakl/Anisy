package com.bhaakl.newsapp.di

import android.content.Context
import androidx.room.Room
import com.bhaakl.newsapp.data.AppDB
import com.bhaakl.newsapp.data.remote.ServiceGenerator
import com.bhaakl.newsapp.data.remote.apiservice.jikan.JikanApiServiceImpl
import com.bhaakl.newsapp.data.remote.apiservice.jikan.JikanApiService
import com.bhaakl.newsapp.domain.util.Network
import com.bhaakl.newsapp.domain.util.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext application: Context): AppDB {
        return Room.databaseBuilder(application, AppDB::class.java, "newsapp_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDB) = db.userDao()

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }

    @Singleton
    @Provides
    fun provideAnimeApiService(
        serviceGenerator: ServiceGenerator,
        networkConnectivity: NetworkConnectivity,
        @ApplicationContext context: Context
    ): JikanApiService {
        return JikanApiServiceImpl(serviceGenerator, networkConnectivity)
    }
}