package com.example.mypets

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

open class MainViewModel:  ViewModel(){

    var auth = FirebaseAuth.getInstance()


}