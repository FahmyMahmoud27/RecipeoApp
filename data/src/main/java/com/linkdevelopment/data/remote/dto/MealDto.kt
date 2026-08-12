package com.linkdevelopment.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MealDto(
    @SerializedName("idMeal")
    val id: String?,

    @SerializedName("strMeal")
    val name: String?,

    @SerializedName("strMealThumb")
    val thumbnail: String?,

    @SerializedName("strCategory")
    val category: String?,

    @SerializedName("strArea")
    val area: String?,

    @SerializedName("strInstructions")
    val instructions: String?,

    @SerializedName("strYoutube")
    val youtubeUrl: String?
)

