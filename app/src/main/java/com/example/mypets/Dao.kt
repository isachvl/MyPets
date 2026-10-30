package com.example.mypets

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(image: user_imagebase)

    @Query("SELECT * FROM Userimage WHERE userId = :userId")
    fun getItem(userId: String): Flow<user_imagebase?>
}