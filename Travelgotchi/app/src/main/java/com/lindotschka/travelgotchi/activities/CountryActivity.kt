package com.lindotschka.travelgotchi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CitiesAdapter
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.adapter.FoodAdapter
import com.lindotschka.travelgotchi.databinding.ActivityCountryBinding
import com.lindotschka.travelgotchi.util.getShortData

class CountryActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    private lateinit var countryName: String
    private lateinit var countryThumb: String
    private lateinit var citiesAdapter: CitiesAdapter
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var countryGeo: String
    private lateinit var countryCulture: ArrayList<String>

    private lateinit var binding: ActivityCountryBinding
    private lateinit var geoTextView: TextView
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

        val foodView = binding.foodView
        foodView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        foodAdapter = FoodAdapter(ArrayList())
        foodView.adapter = foodAdapter

        cultureTextView = findViewById(R.id.culture_info)
        cultureTextView.text = countryCulture?.joinToString(separator = "\n") { "\u2022 $it" }
    }

    private fun getCountryInformation() {
        val intent = intent
        val databaseCities = FirebaseDatabase.getInstance().getReference("cities")
        val databaseFood = FirebaseDatabase.getInstance().getReference("food")

        countryName = intent.getStringExtra(CountriesAdapter.COUNTRY_NAME)!!
        countryThumb = intent.getStringExtra(CountriesAdapter.COUNTRY_THUMB)!!

        getShortData(databaseCities, this, countryName) { cityList ->
            citiesAdapter.updateData(cityList)
        }

        countryGeo = intent.getStringExtra(CountriesAdapter.COUNTRY_GEO)!!

        getShortData(databaseFood, this, countryName) { foodList ->
            foodAdapter.updateData(foodList)
        }

        countryCulture = intent.getStringArrayListExtra(CountriesAdapter.COUNTRY_CULTURE)!!
    }
}