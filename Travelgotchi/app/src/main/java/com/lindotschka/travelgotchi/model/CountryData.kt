package com.lindotschka.travelgotchi.model
data class CountryData(
    val name: String? = null,
    val imageUrl: String? = null,
    val continent: String? =null,
    val info: CountryInfo? = null,
    val cities: List<String>? = null
)

data class CountryInfo(
    val geographicalData: String? = null,
    val foodCulture: List<String>? = null,
    val apps: List<String>? = null,
    val culturalSpecials: List<String>? = null,
    val events: List<String>? = null,
    val innerCountry: List<String>? = null
)