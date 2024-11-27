package com.lindotschka.travelgotchi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.databinding.ActivityCountryBinding

class CountryActivity : AppCompatActivity() {
    private lateinit var countryName: String
    private lateinit var countryThumb: String
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

        getCountryInformation()

        setInformationInViews()
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(countryThumb)
            .into(binding.imgCountryDetail)

        binding.collapsingToolbar.title = countryName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))

        geoTextView = findViewById(R.id.geo_info)
        geoTextView.text = countryGeo

        foodTextView = findViewById(R.id.food_info)
        foodTextView.text = countryFood?.joinToString(separator = "\n") { "\u2022 $it" }

        cultureTextView = findViewById(R.id.culture_info)
        cultureTextView.text = countryCulture?.joinToString(separator = "\n") { "\u2022 $it" }
    }

    private fun getCountryInformation() {
        val intent = intent

        countryName = intent.getStringExtra(CountriesAdapter.COUNTRY_NAME)!!
        countryThumb = intent.getStringExtra(CountriesAdapter.COUNTRY_THUMB)!!
        countryGeo = intent.getStringExtra(CountriesAdapter.COUNTRY_GEO)!!
        countryFood = intent.getStringArrayListExtra(CountriesAdapter.COUNTRY_FOOD)!!
        countryCulture = intent.getStringArrayListExtra(CountriesAdapter.COUNTRY_CULTURE)!!
    }
}