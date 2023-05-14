package com.example.buckbunnymovies.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val videoId: Int,
    val sources: String,
    val thumb: String,
    val subtitle: String,
    val description: String,
    val title: String,
    val isDownloaded: Boolean){

    constructor(): this(-1,"","","","","",false)
}