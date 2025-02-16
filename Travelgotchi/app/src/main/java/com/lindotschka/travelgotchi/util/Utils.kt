package com.lindotschka.travelgotchi.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.lindotschka.travelgotchi.HomeViewModel
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.model.CityAirport
import com.lindotschka.travelgotchi.model.CityData
import com.lindotschka.travelgotchi.model.CityInfo
import com.lindotschka.travelgotchi.model.CountryData
import com.lindotschka.travelgotchi.model.CountryInfo
import com.lindotschka.travelgotchi.model.FoodData
import com.lindotschka.travelgotchi.model.InfraCity
import com.lindotschka.travelgotchi.model.SightsData

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
                    val infosSnapshot = countrySnapshot.child("info")

                    val infos = if (infosSnapshot.exists()) {
                        CountryInfo(
                            geographicalData = infosSnapshot.child("geographicalData").value as? String,
                            foodCulture = infosSnapshot.child("food").getValue(object : GenericTypeIndicator<List<String>>() {}),
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
                            info = infos,
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

inline fun <reified T> getShortData(
    mDataBase: DatabaseReference,
    context: Context,
    upperClass: String,
    crossinline onComplete: (List<T>) -> Unit
) {
    val itemList = ArrayList<T>()

    mDataBase.addListenerForSingleValueEvent(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                itemList.clear()

                for (itemSnapshot in snapshot.children) {
                    val name = itemSnapshot.child("name").value as? String
                    val imageUrl = itemSnapshot.child("imageUrl").value as? String
                    val upperClass2 = itemSnapshot.child("upper_class").value as? String

                    if (name != null && imageUrl != null && upperClass2 == upperClass) {
                        val item = createItem<T>(name, imageUrl, upperClass2)

                        if (item != null) {
                            itemList.add(item)
                        }

                    }
                }
                onComplete(itemList.toList())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context,
                error.message, Toast.LENGTH_SHORT).show()
        }

    })
}

fun getCitiesData(
    mDataBase: DatabaseReference,
    context: Context,
    onComplete: (CityData?) -> Unit
) {

    mDataBase.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val name = snapshot.child("name").value as? String
                val imageUrl = snapshot.child("imageUrl").value as? String
                val country = snapshot.child("upper_class").value as? String
                val infosSnapshot = snapshot.child("info")
                val airportSnapshot = snapshot.child("airport")
                val infraSnapshot = snapshot.child("innerCity")

                val info = if (infosSnapshot.exists()) {
                    CityInfo(
                        sights = infosSnapshot.child("sights")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        discountFree = infosSnapshot.child("discountFree")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        mustPlan = infosSnapshot.child("mustPlan")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        nightlife = infosSnapshot.child("nightlife")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        area = infosSnapshot.child("area")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        apps = infosSnapshot.child("apps")
                            .getValue(object : GenericTypeIndicator<List<String>>() {})
                    )
                } else {
                    null
                }

                val airport = if (airportSnapshot.exists()) {
                    CityAirport(
                        busMetro = airportSnapshot.child("busMetro")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        taxi = airportSnapshot.child("taxi")
                            .getValue(object : GenericTypeIndicator<List<String>>() {})
                    )
                } else {
                    null
                }

                val infra = if (infraSnapshot.exists()) {
                    InfraCity(
                        busMetro = infraSnapshot.child("busMetro")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        walking = infraSnapshot.child("walking")
                            .getValue(object : GenericTypeIndicator<List<String>>() {}),
                        bike = infraSnapshot.child("bike")
                            .getValue(object : GenericTypeIndicator<List<String>>() {})
                    )
                } else {
                    null
                }

                val city = if (name != null && imageUrl != null && country != null) {
                    CityData(
                        name = name,
                        imageUrl = imageUrl,
                        upper_class = country,
                        info = info,
                        airport = airport,
                        innerCity = infra
                    )
                } else {
                    null
                }
                onComplete(city)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(
                context,
                error.message, Toast.LENGTH_SHORT
            ).show()
            onComplete(null)
        }

    })
}

    fun getSightsData(
        mDataBase: DatabaseReference,
        context: Context,
        onComplete: (SightsData?) -> Unit
    ) {

        mDataBase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("name").value as? String
                    val imageUrl = snapshot.child("imageUrl").value as? String
                    val city = snapshot.child("upper_class").value as? String
                    val info = snapshot.child("info").value as? String
                    val price = snapshot.child("price").getValue(object : GenericTypeIndicator<List<String>>() {})
                    val attraction = snapshot.child("attraction").value as? String
                    val near = snapshot.child("near").getValue(object : GenericTypeIndicator<List<String>>() {})
                    val plan = snapshot.child("plan").value as? String

                    val sight = if (name != null && imageUrl != null && city != null) {
                        SightsData(
                            name = name,
                            imageUrl = imageUrl,
                            info = info,
                            price = price,
                            attraction = attraction,
                            near = near,
                            plan = plan,
                            upper_class = city
                        )
                    } else {
                        null
                    }
                    onComplete(sight)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    error.message, Toast.LENGTH_SHORT
                ).show()
                onComplete(null)
            }

        })
    }

    inline fun <reified T> createItem(
        name: String,
        imageUrl: String,
        upperClass: String
    ): T? {
        return when (T::class) {
            CityData::class -> CityData(name, imageUrl, upperClass) as? T
            FoodData::class -> FoodData(name, imageUrl, upperClass) as? T
            SightsData::class -> SightsData(name, imageUrl, upperClass) as? T

            else -> null
        }
    }


