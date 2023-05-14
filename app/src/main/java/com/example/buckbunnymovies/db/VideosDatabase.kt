package com.example.buckbunnymovies.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.buckbunnymovies.db.dao.VideoDao
import com.example.buckbunnymovies.db.entities.VideoEntity

@Database(entities = [VideoEntity::class], version = 1)
abstract class VideosDatabase : RoomDatabase(){
    companion object{
        const val DB_NAME = "buck_bunny.db"
        @Volatile
        private var INSTANCE: VideosDatabase? = null
        fun getInstance(mContext: Context): VideosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    mContext.applicationContext,
                    VideosDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract fun videoDao(): VideoDao
}