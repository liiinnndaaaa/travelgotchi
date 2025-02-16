package com.lindotschka.travelgotchi.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ExpandableListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CitiesAdapter
import com.lindotschka.travelgotchi.adapter.ExpandableListAdapter
import com.lindotschka.travelgotchi.adapter.SightsAdapter
import com.lindotschka.travelgotchi.databinding.ActivityCityBinding
import com.lindotschka.travelgotchi.model.CityAirport
import com.lindotschka.travelgotchi.model.CityInfo
import com.lindotschka.travelgotchi.model.InfraCity
import com.lindotschka.travelgotchi.util.getShortData


class CityActivity : AppCompatActivity() {

    private lateinit var toolbarCity: Toolbar

    private lateinit var cityName: String
    private lateinit var cityImage: String

    private lateinit var infoListAdapter: ExpandableListAdapter
    private lateinit var airportListAdapter: ExpandableListAdapter
    private lateinit var infraListAdapter: ExpandableListAdapter

    private lateinit var datainfo: DatabaseReference
    private lateinit var dataair: DatabaseReference
    private lateinit var datainfra: DatabaseReference

    private lateinit var titleList: List<String>

    private lateinit var sightsAdapter: SightsAdapter

    private lateinit var cityNightlife: ArrayList<String>
    private lateinit var cityApps: ArrayList<String>

    private lateinit var binding: ActivityCityBinding
    private lateinit var nightlifeTextView: TextView
    private lateinit var appsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbarCity = findViewById(R.id.toolbar_city)

        setSupportActionBar(toolbarCity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarCity.setNavigationOnClickListener {
            onBackPressed();
        }

        getCityInformation()

        datainfo = FirebaseDatabase.getInstance().getReference("cities/$cityName/info")
        dataair = FirebaseDatabase.getInstance().getReference("cities/$cityName/airpor")
        datainfra = FirebaseDatabase.getInstance().getReference("cities/$cityName/innerCity")

        fetchInfoFromFirebase(cityName, datainfo)
        fetchInfoFromFirebase(cityName, dataair)
        fetchInfoFromFirebase(cityName, datainfra)

        setInformationInViews()
    }

     private fun fetchInfoFromFirebase(cityName: String, mDatabase: DatabaseReference) {
         var dataList = mapOf<String, List<String>>()

         mDatabase.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Daten extrahieren und zu dataList hinzufügen

                if (mDatabase == datainfo) {
                    val info = CityInfo(
                        discountFree = snapshot.child("discountFree").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        mustPlan = snapshot.child("mustPlan").getValue(object: GenericTypeIndicator<List<String>>() {}),
                    )
                    dataList = mapOf(
                        "Rabatte und Kostenloses" to info.discountFree.orEmpty(),
                        "Unbedingt vorab buchen" to info.mustPlan.orEmpty()
                    ).filter { it.value.isNotEmpty() }

                } else if (mDatabase == dataair) {
                    val info = CityAirport(
                        busMetro = snapshot.child("busMetro").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        taxi = snapshot.child("´taxi").getValue(object : GenericTypeIndicator<List<String>>() {})
                    )
                    dataList = mapOf(
                        "Bus und Bahn" to info.busMetro.orEmpty(),
                        "Taxi" to info.taxi.orEmpty()
                    ).filter { it.value.isNotEmpty() }

                } else if (mDatabase == datainfra) {
                    val info = InfraCity(
                        busMetro = snapshot.child("busMetro").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        walking = snapshot.child("walking").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        bike = snapshot.child("bike").getValue(object : GenericTypeIndicator<List<String>>() {}),
                    )
                    dataList = mapOf(
                        "Bus und Bahn" to info.busMetro.orEmpty(),
                        "zu Fuß" to info.walking.orEmpty(),
                        "mit Fahrrad" to info.bike.orEmpty()
                    ).filter { it.value.isNotEmpty() }
                }

                titleList = dataList.keys.toList()

                // Setup der ExpandableListView nur, wenn es Daten gibt
                if (dataList.isNotEmpty()) {
                    setupExpandableListView(dataList, mDatabase)
                } else {
                    Log.e("CityActivity", "Keine Daten verfügbar")
                }
            } else {
                Log.e("CityActivity", "CityInfo ist null für $cityName")
            }

         }.addOnFailureListener { exception ->
             Log.e("CityActivity", "Error fetching data: ${exception.message}")
         }
     }

    private fun setInformationInViews() {
        Glide.with(this)
            .load(cityImage)
            .into(binding.imgCityDetail)

        binding.collapsingToolbarCity.title = cityName
        binding.collapsingToolbarCity.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbarCity.setExpandedTitleColor(resources.getColor(R.color.white))

        val sightView = binding.sightsView
        sightView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        sightsAdapter = SightsAdapter(ArrayList())
        sightView.adapter = sightsAdapter

        nightlifeTextView = findViewById(R.id.nightlifeInfo)
        nightlifeTextView.text = cityNightlife?.joinToString(separator = "\n") { "\u2022 $it" }

        appsTextView = findViewById(R.id.appsInfo)
        appsTextView.text = cityApps.joinToString(separator = "\n") { "\u2022 $it" }
    }

    private fun setupExpandableListView(dataList: Map<String, List<String>>, mDatabase: DatabaseReference) {
        val listView: ExpandableListView = when (mDatabase) {
            datainfo -> findViewById(R.id.expendableList_infoCity)
            dataair -> findViewById(R.id.expendableList_airport)
            datainfra -> findViewById(R.id.expendableList_infra)
            else -> return
        }

        if (mDatabase == datainfo) {
            infoListAdapter = ExpandableListAdapter(this, dataList)
        } else if (mDatabase == dataair) {
            airportListAdapter = ExpandableListAdapter(this, dataList)
        } else if (mDatabase == datainfra) {
            infraListAdapter = ExpandableListAdapter(this, dataList)
        }

        val expandableListAdapter: ExpandableListAdapter = when (mDatabase) {
            datainfo -> infoListAdapter
            dataair -> airportListAdapter
            datainfra -> infraListAdapter
            else -> return
        }

        listView!!.setAdapter(expandableListAdapter)

        expandableListAdapter.notifyDataSetChanged()

        listView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                listView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setExpandableListViewHeight(listView)
            }
        })


        listView!!.setOnGroupCollapseListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
                Toast.LENGTH_SHORT
            ).show()
            setExpandableListViewHeight(listView)
        }

        listView.setOnGroupExpandListener { groupPosition ->
            Toast.makeText(
                applicationContext,
                (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
                Toast.LENGTH_SHORT
            ).show()
            // Rufe die Hilfsfunktion auf, um die Höhe der expandierten Gruppe anzupassen:
            setExpandableListViewHeight(listView)
        }
        listView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
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

    private fun setExpandableListViewHeight(expandableListView: ExpandableListView) {
        val adapter = expandableListView.expandableListAdapter ?: return
        var totalHeight = 0

        // Gehe durch alle Gruppen
        for (i in 0 until adapter.groupCount) {
            // Hole und messe das Gruppen-Header-View
            val groupItem = adapter.getGroupView(i, false, null, expandableListView)
            groupItem.measure(
                View.MeasureSpec.makeMeasureSpec(expandableListView.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            totalHeight += groupItem.measuredHeight

            // Falls die Gruppe expandiert ist, addiere die Höhe aller Child-Views
            if (expandableListView.isGroupExpanded(i)) {
                for (j in 0 until adapter.getChildrenCount(i)) {
                    val listItem = adapter.getChildView(i, j, false, null, expandableListView)
                    listItem.measure(
                        View.MeasureSpec.makeMeasureSpec(expandableListView.width, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    totalHeight += listItem.measuredHeight
                }
            }
        }

        // Addiere die Höhe der Divider zwischen den Gruppen (falls vorhanden)
        totalHeight += expandableListView.dividerHeight * (adapter.groupCount - 1)

        // Setze die berechnete Höhe in die Layout-Parameter der ExpandableListView
        val params = expandableListView.layoutParams
        params.height = totalHeight
        expandableListView.layoutParams = params
        expandableListView.requestLayout()
    }

    private fun getCityInformation() {
        val intent = intent
        val databaseSights = FirebaseDatabase.getInstance().getReference("sights")

        cityName = intent.getStringExtra(CitiesAdapter.CITY_NAME)!!
        cityImage = intent.getStringExtra(CitiesAdapter.CITY_IMAGE)!!

        getShortData(databaseSights, this, cityName) { sightList ->
            sightsAdapter.updateData(sightList)
        }

        cityNightlife = intent.getStringArrayListExtra(CitiesAdapter.CITY_NIGHTLIFE)!!
        Log.d("CityActivity", "Nightlife wird geladen: $cityNightlife")
        cityApps = intent.getStringArrayListExtra(CitiesAdapter.CITY_APPS)!!

    }
}