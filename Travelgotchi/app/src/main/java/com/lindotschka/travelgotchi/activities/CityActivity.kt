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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ktx.getValue
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.CitiesAdapter
import com.lindotschka.travelgotchi.adapter.ExpandableListAdapter
import com.lindotschka.travelgotchi.adapter.FoodAdapter
import com.lindotschka.travelgotchi.adapter.SightsAdapter
import com.lindotschka.travelgotchi.databinding.ActivityCityBinding
import com.lindotschka.travelgotchi.model.CityAirport
import com.lindotschka.travelgotchi.model.CityData
import com.lindotschka.travelgotchi.model.CityInfo
import com.lindotschka.travelgotchi.model.InfraCity
import com.lindotschka.travelgotchi.model.SightsData
import com.lindotschka.travelgotchi.util.getShortData


class CityActivity : AppCompatActivity() {

    private lateinit var toolbar_city: Toolbar

    private lateinit var cityName: String
    private lateinit var cityThumb: String

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

        toolbar_city = findViewById(R.id.toolbar_city)

        setSupportActionBar(toolbar_city)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_city.setNavigationOnClickListener {
            onBackPressed();
        }

        getCityInformation()

        datainfo = FirebaseDatabase.getInstance().getReference("cities/$cityName/info")
        dataair = FirebaseDatabase.getInstance().getReference("cities/$cityName/airport_to_city")
        datainfra = FirebaseDatabase.getInstance().getReference("cities/$cityName/inner_city")

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
                        discount_free = snapshot.child("discount_free").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        must_plan = snapshot.child("must_plan").getValue(object: GenericTypeIndicator<List<String>>() {}),
                    )
                    dataList = mapOf(
                        "Rabatte und Kostenloses" to info.discount_free.orEmpty(),
                        "Unbedingt vorab buchen" to info.must_plan.orEmpty()
                    ).filter { it.value.isNotEmpty() }

                } else if (mDatabase == dataair) {
                    val info = CityAirport(
                        busbahn = snapshot.child("Bus_Bahn").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        taxi = snapshot.child("Taxi").getValue(object : GenericTypeIndicator<List<String>>() {})
                    )
                    dataList = mapOf(
                        "Bus und Bahn" to info.busbahn.orEmpty(),
                        "Taxi" to info.taxi.orEmpty()
                    ).filter { it.value.isNotEmpty() }

                } else if (mDatabase == datainfra) {
                    val info = InfraCity(
                        transport1 = snapshot.child("Bus_Bahn").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        transport2 = snapshot.child("zu_Fuss").getValue(object : GenericTypeIndicator<List<String>>() {}),
                        transport3 = snapshot.child("mit_Fahrrad").getValue(object : GenericTypeIndicator<List<String>>() {}),
                    )
                    dataList = mapOf(
                        "Bus und Bahn" to info.transport1.orEmpty(),
                        "zu Fuß" to info.transport2.orEmpty(),
                        "mit Fahrrad" to info.transport3.orEmpty()
                    ).filter { it.value.isNotEmpty() }
                }

                titleList = dataList.keys.toList()
                Log.d("CityActivity", "TitleList: ${titleList.joinToString()}")

                dataList.forEach { (key, value) ->
                    Log.d("CityActivity", "Gruppe: $key, Einträge: ${value.joinToString()}")
                }

                // Setup der ExpandableListView nur, wenn es Daten gibt
                if (dataList.isNotEmpty()) {
                    setupExpandableListView(dataList, mDatabase)
                } else {
                    Log.e("CityActivity", "Keine Daten verfügbar")
                }
                Log.d("CityActivity", "DataList: $dataList")
                Log.d("CityActivity", "TitleList: $titleList")
                Log.d("CityActivity", "Snapshot Value: ${snapshot.value}")
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

        nightlifeTextView = findViewById(R.id.nightlife_info)
        nightlifeTextView.text = cityNightlife?.joinToString(separator = "\n") { "\u2022 $it" }

        appsTextView = findViewById(R.id.apps_info)
        appsTextView.text = cityApps.joinToString(separator = "\n") { "\u2022 $it" }
    }

    private fun setupExpandableListView(dataList: Map<String, List<String>>, mDatabase: DatabaseReference) {
        val listView: ExpandableListView = when (mDatabase) {
            datainfo -> findViewById(R.id.expendableList_info_city)
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

        for ((key, value) in dataList) {
            Log.d("CityActivity", "Kategorie: $key, Einträge: ${value.joinToString()}")
        }
        Log.d("CityActivity", "Adapter wird gesetzt mit titleList: $titleList")


        listView!!.setAdapter(expandableListAdapter)

        expandableListAdapter.notifyDataSetChanged()

        listView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Listener entfernen, damit er nicht wiederholt aufgerufen wird
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
        cityThumb = intent.getStringExtra(CitiesAdapter.CITY_THUMB)!!

        getShortData(databaseSights, this, cityName) { sightList ->
            sightsAdapter.updateData(sightList)
        }

        cityNightlife = intent.getStringArrayListExtra(CitiesAdapter.CITY_NIGHTLIFE)!!
        Log.d("CityActivity", "Nightlife wird geladen: $cityNightlife")
        cityApps = intent.getStringArrayListExtra(CitiesAdapter.CITY_APPS)!!

    }
}