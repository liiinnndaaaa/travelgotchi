package com.lindotschka.travelgotchi.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.lindotschka.travelgotchi.HomeViewModel
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.model.CityData
import com.lindotschka.travelgotchi.model.CityInfo
import com.lindotschka.travelgotchi.model.CountryData
import com.lindotschka.travelgotchi.model.CountryInfo
import com.lindotschka.travelgotchi.model.InfraCity

fun getCountriesData(
    mDataBase: DatabaseReference,
    countryList: ArrayList<CountryData>,
    context: Context,
    upperClass: String,
    onComplete: (List<CountryData>) -> Unit
) {
    mDataBase.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                countryList.clear()

                for (countrySnapshot in snapshot.children) {
                    // Name und Image-URL aus jedem Land extrahieren
                    val name = countrySnapshot.child("name").value as? String
                    val imageUrl = countrySnapshot.child("imageUrl").value as? String
                    val continent = countrySnapshot.child("continent").value as? String
                    val infosSnapshot = countrySnapshot.child("infos")

                    val infos = if (infosSnapshot.exists()) {
                        CountryInfo(
                            geographicalData = infosSnapshot.child("geographicalData").value as? String,
                            foodCulture = infosSnapshot.child("essenskultur").getValue(object : GenericTypeIndicator<List<String>>() {}),
                            culturalSpecials = infosSnapshot.child("culturalSpecials").getValue(object : GenericTypeIndicator<List<String>>() {})
                        )
                    } else {
                        null
                    }

                    val cities = countrySnapshot.child("cities").value as? List<String>

                    if (name != null && imageUrl != null && continent == upperClass) {
                        val country = CountryData(
                            name = name,
                            imageUrl = imageUrl,
                            continent = continent,
                            infos = infos,
                            cities = cities
                        )
                        countryList.add(country)
                    }
                }
                onComplete(countryList)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context,
                error.message, Toast.LENGTH_SHORT).show()
        }

    })
}

fun generateCountries(
    countryView: RecyclerView,
    context: Context,
    countriesAdapter: CountriesAdapter,
    viewModel: HomeViewModel,
    continent: String,
    database: DatabaseReference,
) {
    countryView.layoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
    )

    countryView.adapter = countriesAdapter

    viewModel.loadCountries(database, context, continent)
}

fun getCitiesData(
    mDataBase: DatabaseReference,
    context: Context,
    upperClass: String,
    onComplete: (List<CityData>) -> Unit
) {
    val cityList = ArrayList<CityData>()

    mDataBase.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                cityList.clear()

                for (citySnapshot in snapshot.children) {
                    val name = citySnapshot.child("name").value as? String
                    val imageUrl = citySnapshot.child("imageUrl").value as? String
                    val country = citySnapshot.child("country").value as? String
                    val infosSnapshot = citySnapshot.child("info")
                    val airport = citySnapshot.child("airport_to_city").getValue(object: GenericTypeIndicator<List<String>>() {})
                    val infraSnapshot = citySnapshot.child("inner_city")

                    val infos = if (infosSnapshot.exists()) {
                        CityInfo(
                            sights = infosSnapshot.child("sights").getValue(object: GenericTypeIndicator<List<String>>() {}),
                            discount_all = infosSnapshot.child("discount_free_all").getValue(object : GenericTypeIndicator<List<String>>() {}),
                            discount_special = infosSnapshot.child("Rabatte und kostenlos für bestimmte Personengruppen").getValue(object : GenericTypeIndicator<List<String>>() {}),
                            early_booking = infosSnapshot.child("Unbedingt vorab buchen").getValue(object: GenericTypeIndicator<List<String>>() {})
                        )
                    } else {
                        null
                    }

                    val infra = if (infraSnapshot.exists()) {
                        InfraCity(
                            tickets = infraSnapshot.child("Ticketarten").getValue(object: GenericTypeIndicator<List<String>>() {}),
                            apps = infraSnapshot.child("Apps").getValue(object: GenericTypeIndicator<List<String>>() {})
                        )
                    } else {
                        null
                    }

                    if (name != null && imageUrl != null && country == upperClass) {
                        val city = CityData(
                            name = name,
                            imageUrl = imageUrl,
                            country = country,
                            infos = infos,
                            airport_to_city = airport,
                            inner_city = infra
                        )
                        cityList.add(city)
                    }
                }
                Log.d("CityList", "Final city list: ${cityList.size}")
                onComplete(cityList)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context,
                error.message, Toast.LENGTH_SHORT).show()
        }

    })
}

