package com.example.mypets.listpet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class viewmodelNewpets: ViewModel() {
    private val _pets = MutableLiveData<List<InfoPet>>(emptyList())

    val pets: LiveData<List<InfoPet>> = _pets

    fun addPet(pet: InfoPet) {
        _pets.value = _pets.value.orEmpty() + pet
    }
}