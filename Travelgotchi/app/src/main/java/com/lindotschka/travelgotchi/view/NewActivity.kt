package com.lindotschka.travelgotchi.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.uitel.getProgressDrawable
import com.lindotschka.travelgotchi.uitel.loadImage
import com.lindotschka.travelgotchi.databinding.ActivityNewBinding

class NewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        /**get Data */
        val countryIntent = intent
        val countryName = countryIntent.getStringExtra("name")
        val countryInfo = countryIntent.getStringExtra("info")
        val countryImg = countryIntent.getStringExtra("img")

        /**call text and images */
        binding.name.text = countryName
        binding.info.text = countryInfo
        binding.img.loadImage(countryImg, getProgressDrawable(this))
    }
}