package com.linkdevelopment.domain.model

data class Meal(
    val id: String,
    val name: String,
    val imageUrl: String,
    val category: String,
    val area: String,
    val instructions: String,
    val youtubeUrl: String
)