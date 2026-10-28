
package com.example.mypets.Registration

import com.example.mypets.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException

object FirebaseError {

    fun message(error: Exception?): Int? {

        if (error == null) return null

        return when (error) {

            is FirebaseAuthWeakPasswordException ->
                R.string.firebase_error_weak_password

            is FirebaseAuthUserCollisionException ->
                R.string.firebase_error_email_exists

            is FirebaseAuthInvalidUserException ->
                R.string.firebase_error_invalid_credentials

            is FirebaseAuthInvalidCredentialsException ->
                if (error.errorCode == "ERROR_INVALID_EMAIL") {
                    R.string.firebase_error_invalid_email
                } else {
                    R.string.firebase_error_invalid_credentials
                }

            is FirebaseTooManyRequestsException ->
                R.string.firebase_error_too_many_requests

            is FirebaseNetworkException ->
                R.string.firebase_error_network

            is FirebaseFirestoreException ->
                when (error.code) {

                    FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                        R.string.firebase_error_permission

                    FirebaseFirestoreException.Code.UNAVAILABLE ->
                        R.string.firebase_error_unavailable

                    FirebaseFirestoreException.Code.DEADLINE_EXCEEDED ->
                        R.string.firebase_error_timeout

                    else ->
                        R.string.firebase_error_firestore
                }

            else ->
                R.string.firebase_error_unknown
        }
    }
}
