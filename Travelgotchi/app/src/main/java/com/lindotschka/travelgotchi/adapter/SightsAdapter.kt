package com.lindotschka.travelgotchi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.databinding.ItemPicBinding
import com.lindotschka.travelgotchi.model.SightsData

class SightsAdapter(
    private var sightsList: ArrayList<SightsData>
) : RecyclerView.Adapter<SightsAdapter.SightsViewHolder>() {

    companion object{
        const val SIGHT_NAME = "com.lindotschka.travelgotchi.adapter.nameSight"
        const val SIGHT_IMAGE = "com.lindotschka.travelgotchi.adapter.imageSight"
        const val SIGHT_INFOS = "com.lindotschka.travelgotchi.adapter.infosSight"
        const val SIGHT_PREIS = "com.lindotschka.travelgotchi.adapter.preisSight"
        const val SIGHT_HAUPTATTRAKTION = "com.lindotschka.travelgotchi.adapter.hauptattraktionSight"
        const val SIGHT_NAEHE = "com.lindotschka.travelgotchi.adapter.naeheSight"
        const val SIGHT_CITY = "com.lindotschka.travelgotchi.adapter.citySight"
    }

    inner class SightsViewHolder(var v: ItemPicBinding) : RecyclerView.ViewHolder(v.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SightsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemPicBinding>(
            inflater, R.layout.item_pic, parent, false
        )
        return SightsViewHolder(v)
    }

    override fun getItemCount(): Int {
        return sightsList.size
    }

    fun updateData(newList: List<SightsData>) {
        sightsList.clear()
        sightsList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SightsViewHolder, position: Int) {
        val sight = sightsList[position]

        holder.v.picName.text = sight.name

        Glide.with(holder.itemView.context)
            .load(sight.imageUrl)
            .placeholder(R.drawable.ic_continents)
            .error(R.drawable.ic_home)
            .into(holder.v.picImage)

    }
}