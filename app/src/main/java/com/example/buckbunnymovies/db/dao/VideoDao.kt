package com.example.buckbunnymovies.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.buckbunnymovies.db.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("insert or ignore into videos ('videoId','sources','thumb','subtitle','description','title','isDownloaded') values(:videoId,:videoUrl,:thumb,:subtitle,:description,:title,:isDownloaded)")
    fun insert(videoId: Int,videoUrl: String,thumb: String,subtitle: String, description: String, title: String, isDownloaded: Boolean)

    @Query("update videos set isDownloaded = :isDownloaded where videoId = :videoId")
    fun updateIsDownloaded(isDownloaded: Boolean,videoId: Int)

    @Query("select * from videos")
    fun getAllVideos(): Flow<List<VideoEntity>>
}