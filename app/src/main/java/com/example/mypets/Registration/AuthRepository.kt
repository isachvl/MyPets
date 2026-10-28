package com.example.mypets.Registration

import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject


class AuthRepository @Inject constructor() {
    private val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore


    fun SingIn(email: String,password: String,onResult: (AuthResult?, Exception?, Boolean) -> Unit){

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(task.result, null,true)

            } else {

                onResult(null, task.exception,false)
            }
        }

    }
    fun Registration(email: String,password: String,firstname: String,secondName: String, onResult: (AuthResult?, Exception?, Boolean) -> Unit){

        val user = hashMapOf(
            "firstname" to firstname,
            "secondName" to secondName,
            "email" to email
        )

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uid = task.result.user?.uid
                if (uid!=null) {
                    db.collection("users").document(uid).set(user)

                }

                onResult(task.result, null, true)

            }
            else {
                onResult(null, task.exception,false)
            }
        }

    }


}