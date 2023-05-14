package com.example.buckbunnymovies.ui.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

data class Categories(
    val categories: List<CategoriesItem>
)

data class Video(
    val sources: List<String>,
    val thumb: String,
    val subtitle: String,
    val description: String,
    val title: String) {

    companion object {
        @JvmStatic
        @BindingAdapter("setBuckBunnyThumb")
        fun setBuckBunnyThumb(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView.context).load("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/$imageUrl").into(imageView)
        }
    }
}

data class CategoriesItem(
    val name: String,
    val videos: List<Video>
)