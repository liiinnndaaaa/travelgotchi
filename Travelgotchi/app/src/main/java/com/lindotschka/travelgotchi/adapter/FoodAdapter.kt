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
import com.lindotschka.travelgotchi.databinding.ItemPicBinding
import com.lindotschka.travelgotchi.model.FoodData

class FoodAdapter(
    private var foodList: ArrayList<FoodData>,
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    companion object{
        const val FOOD_NAME = "com.lindotschka.travelgotchi.adapter.nameFood"
        const val FOOD_IMAGE = "com.lindotschka.travelgotchi.adapter.imageFood"
        const val FOOD_VEGETARIAN = "com.lindotschka.travelgotchi.adapter.vegetarianFood"
        const val FOOD_VEGAN = "com.lindotschka.travelgotchi.adapter.veganFood"
        const val FOOD_REGION = "com.lindotschka.travelgotchi.adapter.regionFood"
        const val FOOD_COUNTRY = "com.lindotschka.travelgotchi.adapter.countryFood"
    }

    inner class FoodViewHolder(var v: ItemPicBinding) : RecyclerView.ViewHolder(v.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemPicBinding>(
            inflater, R.layout.item_pic, parent, false
        )
        return FoodViewHolder(v)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    fun updateData(newList: List<FoodData>) {
        foodList.clear()
        foodList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]

        holder.v.picName.text = food.name

        Glide.with(holder.itemView.context)
            .load(food.imageUrl)
            .placeholder(R.drawable.ic_continents)
            .error(R.drawable.ic_home)
            .into(holder.v.picImage)

    }
}
