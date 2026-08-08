package com.linkdevelopment.data.di

import com.linkdevelopment.data.repository.RecipeRepositoryImpl
import com.linkdevelopment.domain.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        repositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository
}