package com.example.mypets.listpet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypets.Registration.AuthRepository

class viewmodelNewpets: ViewModel(){
    private val _pets = MutableLiveData<List<InfoPet>>(emptyList())



    private val repository = pushNewpetIndatabase()

    val pets: LiveData<List<InfoPet>> = _pets


    fun addPet(pet: InfoPet) {
        _pets.value = _pets.value.orEmpty() + pet

        repository.addInfoPet(pet)
    }


    fun checkDataBase(){

         repository.takePets {

             pets ->

             _pets.value = pets

         }

    }



}