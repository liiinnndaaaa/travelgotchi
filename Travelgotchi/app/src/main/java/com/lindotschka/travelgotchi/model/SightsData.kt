package com.lindotschka.travelgotchi.model

data class SightsData(
    val name: String? = null,
    val imageUrl: String? = null,
    val infos: String? = null,
    val preis: List<String>? = null,
    val hauptattraktion: String? = null,
    val nähe: List<String>? = null,
    val planen: String? = null,
    val upper_class: String? = null
)
