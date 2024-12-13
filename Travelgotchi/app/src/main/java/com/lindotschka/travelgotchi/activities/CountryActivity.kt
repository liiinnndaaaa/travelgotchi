package com.lindotschka.travelgotchi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CitiesAdapter
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.databinding.ActivityCountryBinding
import com.lindotschka.travelgotchi.model.CityData
import com.lindotschka.travelgotchi.model.CountryData
import com.lindotschka.travelgotchi.util.getCitiesData

class CountryActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    private lateinit var countryName: String
    private lateinit var countryThumb: String
    private lateinit var citiesAdapter: CitiesAdapter
    private lateinit var countryGeo: String
    private lateinit var countryFood: ArrayList<String>
    private lateinit var countryCulture: ArrayList<String>

    private lateinit var binding: ActivityCountryBinding
    private lateinit var geoTextView: TextView
    private lateinit var foodTextView: TextView
    private lateinit var cultureTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed();
        }

        getCountryInformation()

        setInformationInViews()

    }

    private fun setInformationInViews() {
        Glide.with(this)
            .load(countryThumb)
            .into(binding.imgCountryDetail)

        binding.collapsingToolbar.title = countryName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

        val cityView = binding.cityView
        cityView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        citiesAdapter = CitiesAdapter(ArrayList())
        cityView.adapter = citiesAdapter

        geoTextView = findViewById(R.id.geo_info)
        geoTextView.text = countryGeo

        foodTextView = findViewById(R.id.food_info)
        foodTextView.text = countryFood?.joinToString(separator = "\n") { "\u2022 $it" }

        cultureTextView = findViewById(R.id.culture_info)
        cultureTextView.text = countryCulture?.joinToString(separator = "\n") { "\u2022 $it" }
    }

    private fun getCountryInformation() {
        val intent = intent
        val database = FirebaseDatabase.getInstance().getReference("cities")

        countryName = intent.getStringExtra(CountriesAdapter.COUNTRY_NAME)!!
        countryThumb = intent.getStringExtra(CountriesAdapter.COUNTRY_THUMB)!!

        getCitiesData(database, this, countryName) { cityList ->
            citiesAdapter.updateData(cityList)
        }

        countryGeo = intent.getStringExtra(CountriesAdapter.COUNTRY_GEO)!!
        countryFood = intent.getStringArrayListExtra(CountriesAdapter.COUNTRY_FOOD)!!
        countryCulture = intent.getStringArrayListExtra(CountriesAdapter.COUNTRY_CULTURE)!!
    }
}