package com.bhaakl.anisy.di

import com.bhaakl.anisy.data.util.error.mapper.ErrorMapper
import com.bhaakl.anisy.data.util.error.mapper.ErrorMapperSource
import com.bhaakl.anisy.data.util.ErrorManager
import com.bhaakl.anisy.domain.usecase.errors.ErrorUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorUseCase

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperSource
}