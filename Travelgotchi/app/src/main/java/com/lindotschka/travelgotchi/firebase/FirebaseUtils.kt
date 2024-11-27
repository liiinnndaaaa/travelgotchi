package com.lindotschka.travelgotchi.firebase

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.lindotschka.travelgotchi.HomeViewModel
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.model.CountryData
import com.lindotschka.travelgotchi.model.CountryInfo

fun getCountriesData(
    mDataBase: DatabaseReference,
    countryList: ArrayList<CountryData>,
    context: Context,
    upperClass: String,
    onComplete: (List<CountryData>) -> Unit
) {
    mDataBase.addListenerForSingleValueEvent(object:ValueEventListener{
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
                error.message,Toast.LENGTH_SHORT).show()
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