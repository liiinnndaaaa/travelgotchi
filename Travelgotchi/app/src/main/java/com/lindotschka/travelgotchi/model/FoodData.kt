package com.lindotschka.travelgotchi.model

data class FoodData(
    val name: String? = null,
    val imageUrl: String? = null,
    val vegetarian: String? = null,
    val vegan: String? = null,
    val region: List<String>? = null,
    val upper_class: String? = null
)