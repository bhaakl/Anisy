package com.bhaakl.anisy.di

import com.bhaakl.anisy.domain.repository.AnimeRepository
import com.bhaakl.anisy.data.repository.AnimeRepositoryImpl
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