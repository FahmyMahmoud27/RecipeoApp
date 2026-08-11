package com.linkdevelopment.data.mapper

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