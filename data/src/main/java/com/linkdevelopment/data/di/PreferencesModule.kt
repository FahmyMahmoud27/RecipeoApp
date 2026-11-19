package com.linkdevelopment.data.di

import com.linkdevelopment.data.local.preferences.UserPreferencesRepositoryImpl
import com.linkdevelopment.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        implementation: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}


