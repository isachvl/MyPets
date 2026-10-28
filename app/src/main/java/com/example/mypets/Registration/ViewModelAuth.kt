package com.example.mypets.Registration

import android.R
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject

@HiltViewModel
class ViewModelAuth @Inject constructor(private val authRepository: AuthRepository): ViewModel() {




    val mode = MutableLiveData(AuthMode.LOGIN)


    fun showREgistration(){
        mode.value = AuthMode.REGISTRATION
    }
    fun showLogin(){
        mode.value= AuthMode.LOGIN
    }


    fun authrepos(
        authUser: AuthUser,
        onResult: (Boolean, Int?) -> Unit
    ) {

        if (mode.value == AuthMode.LOGIN) {

            authRepository.SingIn(
                authUser.email,
                authUser.password
            ) { result, error, success ->

                if (success) {
                    onResult(true, null)
                } else {
                    onResult(false,  FirebaseError.message(error))
                }
            }

        } else {

            authRepository.Registration(
                authUser.email,
                authUser.password,
                authUser.firstName ?: "",
                authUser.secondName ?: ""
            ) { result, error, success ->

                if (success) {
                    onResult(true, null)
                } else {
                    onResult(false, FirebaseError.message(error))
                }
            }
        }
    }









}