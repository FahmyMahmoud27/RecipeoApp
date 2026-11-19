package com.linkdevelopment.data.di

import com.linkdevelopment.data.local.localdatasource.CachedMealsLocalDataSource
import com.linkdevelopment.data.local.localdatasource.CachedMealsLocalDataSourceImpl
import com.linkdevelopment.data.repository.RecipeRepositoryImpl
import com.linkdevelopment.data.util.CheckNetworkState
import com.linkdevelopment.domain.repository.RecipeRepository
import com.linkdevelopment.domain.util.ICheckNetworkState
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

    @Binds
    @Singleton
    abstract fun bindCachedMealsLocalDataSource(
        localDataSourceImpl: CachedMealsLocalDataSourceImpl
    ): CachedMealsLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCheckNetworkState(
        checkNetworkState: CheckNetworkState
    ): ICheckNetworkState
}