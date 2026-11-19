package com.linkdevelopment.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MealResponseDto(
    @SerializedName("meals")
    val meals: List<MealDto>?
)