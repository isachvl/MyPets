package com.example.mypets

import androidx.room.Database
import androidx.room.RoomDatabase

import android.content.Context

import androidx.room.Room


@Database(
    entities = [user_imagebase::class],
    version = 1,
    exportSchema = false
)
abstract class mainbd : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {

        fun getDb(context: Context): mainbd {
            return Room.databaseBuilder(
                context.applicationContext,
                mainbd::class.java,
                "user_images.db"
            ).build()
        }
    }
}