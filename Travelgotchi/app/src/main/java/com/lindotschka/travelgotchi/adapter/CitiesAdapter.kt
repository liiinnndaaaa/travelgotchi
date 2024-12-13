package com.lindotschka.travelgotchi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.databinding.ItemCityBinding
import com.lindotschka.travelgotchi.model.CityData

class CitiesAdapter(
    private var cityList: ArrayList<CityData>,
) : RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    inner class CityViewHolder(var v:ItemCityBinding): RecyclerView.ViewHolder(v.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemCityBinding>(
            inflter, R.layout.item_city, parent, false)
        return CityViewHolder(v)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun updateData(newList: List<CityData>) {
        cityList.clear()
        cityList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cityList[position]

        holder.v.cityName.text = city.name

        Glide.with(holder.itemView.context)
            .load(city.imageUrl)
            .placeholder(R.drawable.ic_continents)
            .error(R.drawable.ic_home)
            .into(holder.v.cityImage)

        // Klick-Event
        holder.itemView.setOnClickListener {
            // Hier könntest du eine neue Activity starten, um Details zur Stadt zu zeigen.
        }
    }
}