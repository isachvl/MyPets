package com.example.mypets.listpet

import android.graphics.Bitmap

data class InfoPet(
    val name: String,
    val dateBirth: String,
    val typePet: String,
    val breed: String,
    val gender: String,
    val image: Bitmap? = null
)