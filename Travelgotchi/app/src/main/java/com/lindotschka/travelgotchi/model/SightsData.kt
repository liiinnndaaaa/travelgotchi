package com.lindotschka.travelgotchi.model

data class SightsData(
    val name: String? = null,
    val imageUrl: String? = null,
    val info: String? = null,
    val price: List<String>? = null,
    val attraction: String? = null,
    val near: List<String>? = null,
    val plan: String? = null,
    val upper_class: String? = null
)
