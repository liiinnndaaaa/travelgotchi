package com.lindotschka.travelgotchi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lindotschka.travelgotchi.R
import com.lindotschka.travelgotchi.adapter.SightsAdapter
import com.lindotschka.travelgotchi.databinding.ActivitySightsBinding

class SightActivity : AppCompatActivity() {
    private lateinit var sightName: String
    private lateinit var sightImage: String

    private lateinit var sightInfo: String
    private lateinit var sightPrice: ArrayList<String>
    private lateinit var sightAttraction: String
    private lateinit var sightNear: ArrayList<String>
    private lateinit var sightPlan: String

    private lateinit var binding: ActivitySightsBinding
    private lateinit var nameTextView: TextView
    private lateinit var infoTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var attractionTextView: TextView
    private lateinit var nearTextView: TextView
    private lateinit var planTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySightsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val close: ImageButton = findViewById(R.id.btn_close)

        close.setOnClickListener {
            onBackPressed();
        }

        getSightsInformation()

        setInformationInViews()
    }

    private fun setInformationInViews() {
        Glide.with(this)
            .load(sightImage)
            .into(binding.sightImage)

        nameTextView = findViewById(R.id.sightName)
        nameTextView.text = sightName

        infoTextView = findViewById(R.id.sightInfo)
        infoTextView.text = sightInfo

        priceTextView = findViewById(R.id.sightPrice)
        priceTextView.text = sightPrice?.joinToString(separator = "\n") { "\u2022 $it" }

        attractionTextView = findViewById(R.id.sightAttraction)
        attractionTextView.text = sightAttraction

        nearTextView = findViewById(R.id.sightNear)
        nearTextView.text = sightNear?.joinToString(separator = "\n") { "\u2022 $it" }

        planTextView = findViewById(R.id.sightPlan)
        planTextView.text = sightPlan
    }

    private fun getSightsInformation() {
        val intent = intent

        sightName = intent.getStringExtra(SightsAdapter.SIGHT_NAME)!!
        sightImage = intent.getStringExtra(SightsAdapter.SIGHT_IMAGE)!!

        sightInfo = intent.getStringExtra(SightsAdapter.SIGHT_INFOS)!!
        sightPrice = intent.getStringArrayListExtra(SightsAdapter.SIGHT_PREIS)!!
        sightAttraction = intent.getStringExtra(SightsAdapter.SIGHT_HAUPTATTRAKTION)!!
        sightNear = intent.getStringArrayListExtra(SightsAdapter.SIGHT_NAEHE)!!
        sightPlan = intent.getStringExtra(SightsAdapter.SIGHT_PLAN)!!
    }
}