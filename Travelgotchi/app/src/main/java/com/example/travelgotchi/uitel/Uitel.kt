package com.example.travelgotchi.uitel

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.travelgotchi.R

fun getProgressDrawable(c:Context): CircularProgressDrawable {
    return CircularProgressDrawable(c).apply {
        strokeWidth = 5f
        centerRadius = 40f
        start()
    }
}

/** set Images */
fun ImageView.loadImage(url:String?,progressDawable:CircularProgressDrawable) {

    val option = com.bumptech.glide.request.RequestOptions().placeholder(progressDawable)
        .error(R.drawable.ic_continents)

    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(url)
        .into(this)
}
@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url:String) {
    view.loadImage(url, getProgressDrawable(view.context))
}