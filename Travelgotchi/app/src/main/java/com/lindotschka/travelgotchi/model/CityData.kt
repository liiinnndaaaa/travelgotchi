package com.lindotschka.travelgotchi.model

data class CityData(
    val name: String? = null,
    val imageUrl: String? = null,
    val upper_class: String? = null,
    val info: CityInfo? = null,
    val airport_to_city: CityAirport? = null,
    val inner_city: InfraCity? = null,
    val area_city: List<String>? = null,
    val nightlife: List<String>? = null,
    val apps: List<String>? = null
)

data class CityInfo(
    val discount_free: List<String>? = null,
    val must_plan: List<String>? = null,
    val sights: List<String>? = null
)

data class CityAirport(
    val busbahn: List<String>? = null,
    val taxi: List<String>? = null
)

data class InfraCity(
    val transport1: List<String>? = null,
    val transport2: List<String>? = null,
    val transport3: List<String>? = null
)