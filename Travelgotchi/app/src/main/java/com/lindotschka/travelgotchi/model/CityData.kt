package com.lindotschka.travelgotchi.model

data class CityData(
    val name: String? = null,
    val imageUrl: String? = null,
    val country: String? =null,
    val infos: CityInfo? = null,
    val airport_to_city: List<String>? = null,
    val inner_city: InfraCity? = null
)

data class CityInfo(
    val sights: List<String>? = null,
    val discount_all: List<String>? = null,
    val discount_special: List<String>? = null,
    val early_booking: List<String>? = null
)

data class InfraCity(
    val tickets: List<String>? = null,
    val apps: List<String>? = null
)