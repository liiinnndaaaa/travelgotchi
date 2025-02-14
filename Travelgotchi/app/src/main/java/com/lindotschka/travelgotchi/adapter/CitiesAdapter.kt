package com.lindotschka.travelgotchi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.activities.CityActivity
import com.lindotschka.travelgotchi.databinding.ItemPicBinding
import com.lindotschka.travelgotchi.model.CityData
import com.lindotschka.travelgotchi.util.getCitiesData

class CitiesAdapter(
    private var cityList: ArrayList<CityData>,
) : RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    companion object{
        const val CITY_NAME = "com.lindotschka.travelgotchi.adapter.nameCity"
        const val CITY_THUMB = "com.lindotschka.travelgotchi.adapter.thumbCity"
        const val CITY_NIGHTLIFE = "com.lindotschka.travelgotchi.adapter.nightlifeCity"
        const val CITY_APPS = "com.lindotschka.travelgotchi.adapter.appsCity"
    }

    inner class CityViewHolder(var v:ItemPicBinding): RecyclerView.ViewHolder(v.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemPicBinding>(
            inflter, R.layout.item_pic, parent, false)
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

        holder.v.picName.text = city.name

        Glide.with(holder.itemView.context)
            .load(city.imageUrl)
            .placeholder(R.drawable.ic_continents)
            .error(R.drawable.ic_home)
            .into(holder.v.picImage)

        // Klick-Event
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val database = FirebaseDatabase.getInstance().getReference("cities/${city.name}")
            val intent = Intent(context, CityActivity::class.java)



            getCitiesData(database, context) { cityData ->
                if (cityData != null) {
                    intent.putExtra(CITY_NAME,city.name)
                    intent.putExtra(CITY_THUMB,city.imageUrl)

                    intent.putStringArrayListExtra(
                            CITY_NIGHTLIFE,
                    ArrayList(cityData.info?.nightlife ?: emptyList()))

                    intent.putStringArrayListExtra(
                        CITY_APPS,
                        ArrayList(cityData.info?.apps ?: emptyList()))

                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Daten konnten nicht geladen werden", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}