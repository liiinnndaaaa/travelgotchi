package com.lindotschka.travelgotchi.firebase

import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.lindotschka.travelgotchi.adapter.CountriesAdapter
import com.lindotschka.travelgotchi.model.CountryData

fun getCountriesData(
    mDataBase: DatabaseReference,
    countryList: ArrayList<CountryData>,
    mAdapter: CountriesAdapter,
    context: android.content.Context,
    upperClass: String
) {
    mDataBase.addValueEventListener(object:ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                countryList.clear()
                for (countrySnapshot in snapshot.children) {
                    // Name und Image-URL aus jedem Land extrahieren
                    val name = countrySnapshot.child("name").value as? String
                    val imageUrl = countrySnapshot.child("imageUrl").value as? String
                    val continent = countrySnapshot.child("continent").value as? String
                    if (name != null && imageUrl != null && continent == upperClass) {
                        val country = CountryData(
                            name = name,
                            imageUrl = imageUrl
                        )
                        countryList.add(country)
                    }
                }
                mAdapter.notifyDataSetChanged()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context,
                error.message,Toast.LENGTH_SHORT).show()
        }

    })
}