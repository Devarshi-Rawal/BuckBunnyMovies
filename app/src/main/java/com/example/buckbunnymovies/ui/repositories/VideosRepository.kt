package com.example.buckbunnymovies.ui.repositories

import com.example.buckbunnymovies.db.dao.VideoDao
import com.example.buckbunnymovies.db.entities.VideoEntity
import kotlinx.coroutines.flow.Flow

class VideosRepository(private val videoDao: VideoDao) {

    suspend fun insert(videoId: Int,videoUrl: String,thumb: String,subtitle: String, description: String, title: String, isDownloaded: Boolean){
        videoDao.insert(videoId,videoUrl,thumb,subtitle,description,title,isDownloaded)
    }

    fun updateIsDownloaded(isDownloaded: Boolean,videoInt: Int){
        videoDao.updateIsDownloaded(isDownloaded,videoInt)
    }

    fun getAllVideos():Flow<List<VideoEntity>> = videoDao.getAllVideos()
}