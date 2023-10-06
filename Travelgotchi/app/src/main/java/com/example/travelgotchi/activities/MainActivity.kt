package com.example.travelgotchi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelgotchi.R
import com.example.travelgotchi.adapter.CountriesAdapter
import com.example.travelgotchi.model.CountryData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var mDataBase:DatabaseReference
    private lateinit var countryList: ArrayList<CountryData>
    private lateinit var mAdapter: CountriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**initialized */
        countryList = ArrayList()
        mAdapter = CountriesAdapter(this, countryList)
        val recyclerCountries = findViewById<RecyclerView>(R.id.recyclerCountries)
        recyclerCountries.layoutManager = LinearLayoutManager(this)
        recyclerCountries.setHasFixedSize(true)
        recyclerCountries.adapter = mAdapter

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.btm_nav)
        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(bottomNavigation, navController)

        /**getData firebase */
        getCountriesData()
    }

    private fun getCountriesData() {
        val recyclerCountries = findViewById<RecyclerView>(R.id.recyclerCountries)
        mDataBase = FirebaseDatabase.getInstance().getReference("Countries")
        mDataBase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (countrySnapshot in snapshot.children) {
                        val country = countrySnapshot.getValue(CountryData::class.java)
                        countryList.add(country!!)
                    }
                    recyclerCountries.adapter = mAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}