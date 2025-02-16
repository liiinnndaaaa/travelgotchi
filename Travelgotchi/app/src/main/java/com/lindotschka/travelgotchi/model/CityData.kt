package com.lindotschka.travelgotchi.model

data class CityData(
    val name: String? = null,
    val imageUrl: String? = null,
    val upper_class: String? = null,
    val info: CityInfo? = null,
    val airport: CityAirport? = null,
    val innerCity: InfraCity? = null
)

data class CityInfo(
    val discountFree: List<String>? = null,
    val mustPlan: List<String>? = null,
    val nightlife: List<String>? = null,
    val area: List<String>? = null,
    val apps: List<String>? = null,
    val sights: List<String>? = null,
)

data class CityAirport(
    val busMetro: List<String>? = null,
    val taxi: List<String>? = null
)

data class InfraCity(
    val busMetro: List<String>? = null,
    val walking: List<String>? = null,
    val bike: List<String>? = null
)