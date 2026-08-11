package com.linkdevelopment.data.di

import android.content.Context
import androidx.room.Room
import com.linkdevelopment.data.local.dao.CachedMealsDao
import com.linkdevelopment.data.local.dao.FavoriteMealsDao
import com.linkdevelopment.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "recipeo_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoriteMealsDao(
        database: AppDatabase
    ): FavoriteMealsDao {
        return database.favoriteMealsDao()
    }

    @Provides
    fun provideCachedMealsDao(
        database: AppDatabase
    ): CachedMealsDao {
        return database.cachedMealsDao()
    }
}
