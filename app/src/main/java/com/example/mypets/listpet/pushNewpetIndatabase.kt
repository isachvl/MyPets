package com.example.mypets.listpet

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class pushNewpetIndatabase{

    private val auth = FirebaseAuth.getInstance()

    val db = Firebase.firestore

    fun addInfoPet(info: InfoPet){
        val uid = auth.currentUser?.uid
        val pet = hashMapOf(

            "name" to info.name,
            "dateBirth" to info.dateBirth,
            "typePet" to info.typePet,
            "breed" to info.breed,
            "gender" to info.gender

        )

        // TODO: Загрузка фотографии в облачное хранилище
        // TODO: Получение URL загруженной фотографии
        // TODO: Сохранение imageUrl вместе с данными питомца в Firestore


        if (uid!=null){
            db.collection("users").document(uid).collection("pets").add(pet)
        }


    }

    fun takePets(onResult: (List<InfoPet>)-> Unit){
        val uid = auth.currentUser?.uid
        if (uid!=null){
        db.collection("users")
            .document(uid)
            .collection("pets")
            .get()
            .addOnSuccessListener { documents ->

                val pets = documents.map { document ->

                    InfoPet(
                        name = document.getString("name") ?: "",
                        dateBirth = document.getString("dateBirth") ?: "",
                        typePet = document.getString("typePet") ?: "",
                        breed = document.getString("breed") ?: "",
                        gender = document.getString("gender") ?: ""
                    )
                }

                onResult(pets)
            }
        }

    }



}