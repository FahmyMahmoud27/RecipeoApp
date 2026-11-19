package com.linkdevelopment.data.mapper

import com.linkdevelopment.data.local.entity.CachedMealEntity
import com.linkdevelopment.data.remote.dto.MealDto
import com.linkdevelopment.domain.model.Meal

fun MealDto.toMeal(): Meal {
    return Meal(
        id = id.orEmpty(),
        name = name.orEmpty(),
        imageUrl = thumbnail.orEmpty(),
        category = category.orEmpty(),
        area = area.orEmpty(),
        instructions = instructions.orEmpty(),
        youtubeUrl = youtubeUrl.orEmpty()
    )
}

fun MealDto.toCachedEntity(categoryName: String? = null): CachedMealEntity {
    return CachedMealEntity(
        id = id.orEmpty(),
        name = name.orEmpty(),
        imageUrl = thumbnail.orEmpty(),
        category = categoryName ?: category.orEmpty(),
        area = area.orEmpty(),
        instructions = instructions.orEmpty(),
        youtubeUrl = youtubeUrl.orEmpty()
    )
}

fun CachedMealEntity.toMeal(): Meal {
    return Meal(
        id = id,
        name = name,
        imageUrl = imageUrl,
        category = category,
        area = area,
        instructions = instructions,
        youtubeUrl = youtubeUrl
    )
}

fun Meal.toCachedEntity(): CachedMealEntity {
    return CachedMealEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        category = category,
        area = area,
        instructions = instructions,
        youtubeUrl = youtubeUrl
    )
}