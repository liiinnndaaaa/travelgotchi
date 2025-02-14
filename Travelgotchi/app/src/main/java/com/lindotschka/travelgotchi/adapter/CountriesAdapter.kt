package com.lindotschka.travelgotchi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.activities.CountryActivity
import com.lindotschka.travelgotchi.databinding.ItemCountryBinding
import com.lindotschka.travelgotchi.model.CountryData

class CountriesAdapter(
    private var countryList: ArrayList<CountryData>,
) :RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>()

{
    companion object{
        const val COUNTRY_NAME = "com.lindotschka.travelgotchi.adapter.nameCountry"
        const val COUNTRY_THUMB = "com.lindotschka.travelgotchi.adapter.thumbCountry"
        const val COUNTRY_GEO = "com.lindotschka.travelgotchi.adapter.geoCountry"
        const val COUNTRY_CULTURE = "com.lindotschka.travelgotchi.adapter.cultureCountry"
    }
    inner class CountryViewHolder(var v:ItemCountryBinding): RecyclerView.ViewHolder(v.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemCountryBinding>(
            inflter, R.layout.item_country, parent, false)
        return CountryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun updateData(newList: List<CountryData>) {
        countryList.clear()
        countryList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countryList[position]
        val culture = country.infos?.culturalSpecials ?: emptyList()

        // Name setzen
        holder.v.countryName.text = country.name

        // Bild mit Glide laden
        Glide.with(holder.itemView.context)
            .load(country.imageUrl) // URL des Bildes
            .placeholder(R.drawable.ic_continents) // Optional: Platzhalter-Bild
            .error(R.drawable.ic_home) // Optional: Fehler-Bild
            .into(holder.v.countryImage)

        // Klick-Event hinzufügen
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CountryActivity::class.java)
            intent.putExtra(COUNTRY_NAME,country.name)
            intent.putExtra(COUNTRY_THUMB,country.imageUrl)

            intent.putExtra(COUNTRY_GEO,country.infos?.geographicalData)
            intent.putStringArrayListExtra(
                COUNTRY_CULTURE,
                ArrayList(culture))

            holder.itemView.context.startActivity(intent)
        }
    }
}