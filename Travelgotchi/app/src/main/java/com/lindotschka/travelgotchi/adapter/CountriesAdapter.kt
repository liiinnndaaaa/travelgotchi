package com.lindotschka.travelgotchi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.databinding.ItemCountryBinding
import com.lindotschka.travelgotchi.fragments.HomeFragment
import com.lindotschka.travelgotchi.model.CountryData

class CountriesAdapter(
    var c: HomeFragment, var countryList:ArrayList<CountryData>
) :RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>()
{
    inner class CountryViewHolder(var v:ItemCountryBinding): RecyclerView.ViewHolder(v.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemCountryBinding>(
            inflter, R.layout.item_country, parent, false)
        return CountryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countryList[position]

        // Name setzen
        holder.v.cityName.text = country.name

        // Bild mit Glide laden
        Glide.with(holder.itemView.context)
            .load(country.imageUrl) // URL des Bildes
            .placeholder(R.drawable.ic_continents) // Optional: Platzhalter-Bild
            .error(R.drawable.ic_home) // Optional: Fehler-Bild
            .into(holder.v.countryImage)

        // Klick-Event hinzufügen
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Clicked on ${country.name}", Toast.LENGTH_SHORT).show()
        }
    }
}