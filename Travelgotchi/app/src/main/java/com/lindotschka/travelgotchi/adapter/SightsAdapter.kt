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
import com.lindotschka.travelgotchi.activities.SightActivity
import com.lindotschka.travelgotchi.databinding.ItemPicBinding
import com.lindotschka.travelgotchi.model.SightsData
import com.lindotschka.travelgotchi.util.getSightsData

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
        const val SIGHT_PLAN = "com.lindotschka.travelgotchi.adapter.planSight"
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

        // Klick-Event
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val database = FirebaseDatabase.getInstance().getReference("sights/${sight.name}")
            val intent = Intent(context, SightActivity::class.java)

            getSightsData(database, context) { sightsData ->
                if (sightsData != null) {
                    intent.putExtra(SIGHT_NAME,sight.name)
                    intent.putExtra(SIGHT_IMAGE,sight.imageUrl)

                    intent.putExtra(SIGHT_INFOS,sightsData.info)

                    intent.putStringArrayListExtra(
                        SIGHT_PREIS,
                        ArrayList(sightsData.price ?: emptyList()))

                    intent.putExtra(SIGHT_HAUPTATTRAKTION,sightsData.attraction)
                    intent.putStringArrayListExtra(
                        SIGHT_NAEHE,
                        ArrayList(sightsData.near ?: emptyList()))

                    intent.putExtra(SIGHT_PLAN,sightsData.plan)

                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Daten konnten nicht geladen werden", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}