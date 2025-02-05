package com.lindotschka.travelgotchi.activities

import android.os.Bundle
import android.util.Log
import android.widget.ExpandableListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CitiesAdapter
import com.lindotschka.travelgotchi.adapter.ExpandableListAdapter
import com.lindotschka.travelgotchi.adapter.FoodAdapter
import com.lindotschka.travelgotchi.adapter.SightsAdapter
import com.lindotschka.travelgotchi.databinding.ActivityCityBinding
import com.lindotschka.travelgotchi.model.CityInfo
import com.lindotschka.travelgotchi.model.SightsData
import com.lindotschka.travelgotchi.util.getShortData


class CityActivity : AppCompatActivity() {

    private lateinit var toolbar_city: Toolbar

    private lateinit var cityName: String
    private lateinit var cityThumb: String

    private lateinit var expandableListAdapter: ExpandableListAdapter
    private lateinit var expandableListView: ExpandableListView

    private lateinit var titleList: List<String>

    private lateinit var cityAirport: ArrayList<String>
    private lateinit var cityArea: ArrayList<String>

    private lateinit var sightsAdapter: SightsAdapter
    private lateinit var citySights: ArrayList<String>
    private lateinit var cityAll: ArrayList<String>

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

        fetchInfoFromFirebase(cityName)

        setInformationInViews()
    }

    private fun fetchInfoFromFirebase(cityName: String) {
        val database = FirebaseDatabase.getInstance().getReference("cities/$cityName/info")

        database.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Mappe die Firebase-Daten auf CityData

                val info = CityInfo(
                    discount_all = snapshot.child("discount_free_all").getValue(object : GenericTypeIndicator<List<String>>() {}),
                    discount_special = snapshot.child("discount_some").getValue(object : GenericTypeIndicator<List<String>>() {}),
                    must_plan = snapshot.child("must_plan").getValue(object: GenericTypeIndicator<List<String>>() {}),
                    apps = snapshot.child("Apps").getValue(object: GenericTypeIndicator<List<String>>() {})
                )

                // Daten extrahieren und zu dataList hinzufügen
                val dataList = mapOf(
                    "Discount and free for all" to info.discount_all.orEmpty(),
                    "Discount and free for special groups" to info.discount_special.orEmpty(),
                    "Must-Plan" to info.must_plan.orEmpty(),
                    "Apps" to info.apps.orEmpty()
                ).filter { it.value.isNotEmpty() }
                Log.d("CityActivity", "Gefilterte dataList: $dataList")

                // Logge die titleList
                titleList = dataList.keys.toList()
                Log.d("CityActivity", "TitleList: ${titleList.joinToString()}")

                // Logge jede Gruppe und ihre Werte
                dataList.forEach { (key, value) ->
                    Log.d("CityActivity", "Gruppe: $key, Einträge: ${value.joinToString()}")
                }

                // Setup der ExpandableListView nur, wenn es Daten gibt
                if (dataList.isNotEmpty()) {
                    setupExpandableListView(dataList)
                } else {
                    Log.e("CityActivity", "Keine Daten verfügbar")
                }
                Log.d("CityActivity", "DataList: $dataList")
                Log.d("CityActivity", "TitleList: $titleList")
                Log.d("CityActivity", "Snapshot Value: ${snapshot.value}")
                Log.d("CityActivity", "Sights: ${snapshot.child("sights").value}")
                Log.d("CityActivity", "Discount Free All: ${snapshot.child("discount_free_all").value}")
            } else {
                Log.e("CityActivity", "CityInfo ist null für $cityName")
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

        val sightView = binding.sightsView
        sightView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sightsAdapter = SightsAdapter(ArrayList())
        sightView.adapter = sightsAdapter

        airportTextView = findViewById(R.id.airport_info)
        airportTextView.text = cityAirport.joinToString(separator = "\n") { "\u2022 $it" }

        areaTextView = findViewById(R.id.area_info)
        areaTextView.text = cityArea.joinToString(separator = "\n") { "\u2022 $it" }
    }

    private fun setupExpandableListView(dataList: Map<String, List<String>>) {
        expandableListView = findViewById(R.id.expendableList_info_city)

        expandableListAdapter = ExpandableListAdapter(this, dataList)

        for ((key, value) in dataList) {
            Log.d("CityActivity", "Kategorie: $key, Einträge: ${value.joinToString()}")
        }
        Log.d("CityActivity", "Adapter wird gesetzt mit titleList: $titleList")
        expandableListView!!.setAdapter(expandableListAdapter)

        expandableListAdapter.notifyDataSetChanged()

        expandableListView!!.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
                Toast.LENGTH_SHORT
            ).show()
        }

        expandableListView!!.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
                Toast.LENGTH_SHORT
            ).show()
        }
        expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
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
        val databaseSights = FirebaseDatabase.getInstance().getReference("sights")

        cityName = intent.getStringExtra(CitiesAdapter.CITY_NAME)!!
        cityThumb = intent.getStringExtra(CitiesAdapter.CITY_THUMB)!!

        getShortData(databaseSights, this, cityName) { sightList ->
            sightsAdapter.updateData(sightList)
        }

        cityAirport = intent.getStringArrayListExtra(CitiesAdapter.CITY_AIRPORT)!!
        cityArea = intent.getStringArrayListExtra(CitiesAdapter.CITY_AREA)!!

        citySights = intent.getStringArrayListExtra(CitiesAdapter.CITY_SIGHTS)!!
        cityAll = intent.getStringArrayListExtra(CitiesAdapter.CITY_ALL)!!
    }
}