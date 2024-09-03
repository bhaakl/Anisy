package com.bhaakl.newsapp.di

import com.bhaakl.newsapp.domain.repository.AnimeRepository
import com.bhaakl.newsapp.domain.repository.AnimeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun provideDataRepository(animeRepository: AnimeRepositoryImpl): AnimeRepository
}