package com.example.mypets.Registration

import android.R
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypets.listpet.userinfo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject

@HiltViewModel
class ViewModelAuth @Inject constructor(private val authRepository: AuthRepository): ViewModel() {


    val image = MutableLiveData<Bitmap>()

    val mode = MutableLiveData(AuthMode.LOGIN)
    private val _user = MutableLiveData<userinfo>()
    val user: LiveData<userinfo> = _user

    suspend fun updateUser(data: AuthUser): Int {
        return authRepository.updateUser(data)
    }
    fun reauthenticate(
        oldPassword: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        authRep.reauthenticate(oldPassword) { success, error ->
            onResult(success, error)
        }
    }
    fun changePasswordr(
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        authRepository.changePassword(password) { success, message ->
            onResult(success, message)
        }
    }


    fun showREgistration(){
        mode.value = AuthMode.REGISTRATION
    }
    fun showLogin(){
        mode.value= AuthMode.LOGIN
    }



    private val authRep = AuthRepository()

    fun loadUser() {
        authRep.takeUser { userInfo ->
            _user.value = userInfo
        }
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