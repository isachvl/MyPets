package com.example.mypets.Registration

import com.example.mypets.listpet.userinfo
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.example.mypets.R
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
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


    fun reauthenticate(
        oldPassword: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null || user.email == null) {
            onResult(false, "Пользователь не авторизован")
            return
        }

        val credential = EmailAuthProvider.getCredential(
            user.email!!,
            oldPassword
        )

        user.reauthenticate(credential)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { error ->
                onResult(false, error.localizedMessage)
            }
    }
    fun changePassword(
        newPassword: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val user = auth.currentUser

        if (user == null) {
            onResult(false, "Пользователь не авторизован")
            return
        }

        if (newPassword.length < 6) {
            onResult(false, "Пароль должен содержать минимум 6 символов")
            return
        }

        user.updatePassword(newPassword)
            .addOnSuccessListener {
                onResult(true, "Пароль изменён")
            }
            .addOnFailureListener { error ->
                onResult(false, error.localizedMessage ?: "Ошибка смены пароля")
            }
    }

    fun takeUser(onResult: (userinfo) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                val user = userinfo(
                    firstName = document.getString("firstname") ?: "",
                    secondName = document.getString("secondName") ?: "",
                    email = auth.currentUser?.email ?: ""

                )

                onResult(user)
            }
    }
    suspend fun updateUser(data: AuthUser): Int {
        val user = auth.currentUser
            ?: return R.string.user_not_authorized

        val email = data.email.trim()

        if (email.isBlank()) {
            return R.string.enter_email
        }
        if (data.password!=null) {
            if (data.password.isNotEmpty() && data.password.length < 6) {
                return R.string.password_too_short
            }
        }

        return try {
            if (data.password!=null) {
                if (data.password.isNotEmpty()) {
                    user.updatePassword(data.password).await()
                }
            }

            db.collection("users")
                .document(user.uid)
                .set(
                    mapOf(
                        "firstname" to data.firstName?.trim(),
                        "secondName" to data.secondName?.trim(),
                        "email" to (user.email ?: "")
                    ),
                    SetOptions.merge()
                )
                .await()

            if (email != user.email) {
                user.verifyBeforeUpdateEmail(email).await()
                R.string.user_confirm_email
            } else {
                R.string.user_updated
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("UpdateUser", "Ошибка сохранения пользователя", e)
            R.string.user_update_error
        }
    }






}