package com.lindotschka.travelgotchi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CitiesAdapter
import com.lindotschka.travelgotchi.adapter.ExpandableListAdapter
import com.lindotschka.travelgotchi.databinding.ActivityCityBinding
import com.lindotschka.travelgotchi.model.CityData
import com.lindotschka.travelgotchi.model.CityInfo
import com.lindotschka.travelgotchi.util.getCitiesData

class CityActivity : AppCompatActivity() {

    private lateinit var toolbar_city: Toolbar

    private lateinit var cityName: String
    private lateinit var cityThumb: String

    private lateinit var expandableListAdapter: ExpandableListAdapter

    private lateinit var titleList: List<String>
    private lateinit var cityInfo: ExpandableListView
    private lateinit var cityAirport: ArrayList<String>
    private lateinit var cityArea: ArrayList<String>
    private lateinit var citySights: ArrayList<String>

    private lateinit var binding: ActivityCityBinding
    private lateinit var airportTextView: TextView
    private lateinit var areaTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar_city = findViewById(R.id.toolbar_city)

        setSupportActionBar(toolbar_city)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_city.setNavigationOnClickListener {
            onBackPressed();
        }

        getCityInformation()

        setInformationInViews()

        fetchInfoFromFirebase(cityName)
    }

    private fun fetchInfoFromFirebase(cityName: String) {
        val database = FirebaseDatabase.getInstance().getReference("cities/$cityName")

        database.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Mappe die Firebase-Daten auf CityData
                val cityData = snapshot.getValue(CityData::class.java)

                if (cityData?.infos != null) {
                    val infos = CityInfo(
                        sights = snapshot.child("sights").getValue(object: GenericTypeIndicator<List<String>>() {}),
                        discount_all = snapshot.child("discount_free_all").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        discount_special = snapshot.child("discount_some").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        must_plan = snapshot.child("must_plan").getValue(object: GenericTypeIndicator<List<String>>() {}),
                        apps = snapshot.child("Apps").getValue(object: GenericTypeIndicator<List<String>>() {})
                    )

                    // Daten extrahieren und zu dataList hinzufügen
                    val dataList = mapOf(
                        "Sights" to infos.sights.orEmpty(),
                        "Discount and free for all" to infos.discount_all.orEmpty(),
                        "Discount and free for special groups" to infos.discount_special.orEmpty(),
                        "Must-Plan" to infos.must_plan.orEmpty(),
                        "Apps" to infos.apps.orEmpty()
                    ).filter { it.value.isNotEmpty() }

                    if (dataList.isNotEmpty()) {
                        titleList = dataList.keys.toList()
                        setupExpandableListView(dataList)
                    } else {
                        Log.e("CityActivity", "Keine Daten verfügbar")
                    }
                } else {
                    Log.e("CityActivity", "CityInfo ist null für $cityName")
                }
            } else {
                Log.e("CityActivity", "Snapshot existiert nicht für $cityName")
            }
        }.addOnFailureListener { exception ->
            Log.e("CityActivity", "Error fetching data: ${exception.message}")
        }
    }

    private fun setInformationInViews() {
        Glide.with(this)
            .load(cityThumb)
            .into(binding.imgCityDetail)

        binding.collapsingToolbarCity.title = cityName
        binding.collapsingToolbarCity.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbarCity.setExpandedTitleColor(resources.getColor(R.color.white))

        airportTextView = findViewById(R.id.airport_info)
        airportTextView.text = cityAirport.joinToString(separator = "\n") { "\u2022 $it" }

        areaTextView = findViewById(R.id.area_info)
        areaTextView.text = cityArea.joinToString(separator = "\n") { "\u2022 $it" }
    }

    private fun setupExpandableListView(dataList: Map<String, List<String>>) {
        cityInfo = findViewById(R.id.expendableList_info_city)

        expandableListAdapter = ExpandableListAdapter(this, dataList.keys.toList(), dataList)

        cityInfo!!.setAdapter(expandableListAdapter)
        cityInfo!!.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
                Toast.LENGTH_SHORT
            ).show()
        }

        cityInfo!!.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
                Toast.LENGTH_SHORT
            ).show()
        }
        cityInfo!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            val group = titleList[groupPosition]
            val child = dataList[group]?.get(childPosition) ?: "N/A"
            Toast.makeText(
                applicationContext,
                "Clicked: $group -> $child",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    private fun getCityInformation() {
        val intent = intent

        cityName = intent.getStringExtra(CitiesAdapter.CITY_NAME)!!
        cityThumb = intent.getStringExtra(CitiesAdapter.CITY_THUMB)!!

        cityAirport = intent.getStringArrayListExtra(CitiesAdapter.CITY_AIRPORT)!!
        cityArea = intent.getStringArrayListExtra(CitiesAdapter.CITY_AREA)!!
        citySights = intent.getStringArrayListExtra(CitiesAdapter.CITY_SIGHTS)!!
    }
}