package com.linkdevelopment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_meals")
data class CachedMealEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String,
    val category: String,
    val area: String,
    val instructions: String,
    val youtubeUrl: String
)
