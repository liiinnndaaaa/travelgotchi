package com.example.travelgotchi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelgotchi.R
import com.example.travelgotchi.databinding.FragmentContinentsBinding
import com.example.travelgotchi.model.CountryData

class CountriesAdapter(
    var c: Context, var countryList: ArrayList<CountryData>
) :RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>()
{
    inner class CountryViewHolder(var v:FragmentContinentsBinding):RecyclerView.ViewHolder(v.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<FragmentContinentsBinding>(inflter,
            R.layout.fragment_continents,parent,false )
        return CountryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.v.isCountries = countryList[position]
    }
}