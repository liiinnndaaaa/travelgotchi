package com.lindotschka.travelgotchi

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.lindotschka.travelgotchi.model.CountryData
import com.lindotschka.travelgotchi.util.getCountriesData

class HomeViewModel : ViewModel() {

    private val _europeCountries = MutableLiveData<List<CountryData>>()
    val europeCountries: LiveData<List<CountryData>> get() = _europeCountries

    private val _asiaCountries = MutableLiveData<List<CountryData>>()
    val asiaCountries: LiveData<List<CountryData>> get() = _asiaCountries

    private val _northamericaCountries = MutableLiveData<List<CountryData>>()
    val northamericaCountries: LiveData<List<CountryData>> get() = _northamericaCountries

    fun loadCountries(mDataBase: DatabaseReference,
                      context: Context,
                      continent: String) {
        val countryList = ArrayList<CountryData>()

        getCountriesData(mDataBase, countryList, context, continent) { countryList ->
            when (continent) {
                "europe" -> _europeCountries.value = countryList
                "asia" -> _asiaCountries.value = countryList
                "northamerica" -> _northamericaCountries.value = countryList
            }
        }
    }
}

