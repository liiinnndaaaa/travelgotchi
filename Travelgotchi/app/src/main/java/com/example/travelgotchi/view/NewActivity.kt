package com.example.travelgotchi.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelgotchi.R
import com.example.travelgotchi.uitel.getProgressDrawable
import com.example.travelgotchi.uitel.loadImage
import com.example.travelgotchi.databinding.ActivityNewBinding

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