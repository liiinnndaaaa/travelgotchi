package com.example.travelgotchi.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelgotchi.R
import com.example.travelgotchi.databinding.FragmentContinentsBinding
import com.example.travelgotchi.model.CountryData
import com.example.travelgotchi.view.NewActivity

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
        val newList = countryList[position]
        holder.v.isCountries = countryList[position]
        holder.v.root.setOnClickListener {
            val img = newList.img
            val name = newList.name
            val info = newList.info

            /** set Data */

            val mIntent = Intent(c,NewActivity::class.java)
            mIntent.putExtra("img", img)
            mIntent.putExtra("name", name)
            mIntent.putExtra("info", info)
            c.startActivity(mIntent)
        }
    }
}