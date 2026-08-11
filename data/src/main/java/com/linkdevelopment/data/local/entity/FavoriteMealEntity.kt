package com.linkdevelopment.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "favorite_meals")
data class FavoriteMealEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val imageUrl: String,
    val category: String,
    val area: String
)

data class FavoriteMealWithCached(
    @Embedded
    val favorite: FavoriteMealEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val cached: CachedMealEntity?
)



