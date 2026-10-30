package com.example.mypets

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Userimage")
data class user_imagebase (

    @PrimaryKey
    val userId: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray,

)