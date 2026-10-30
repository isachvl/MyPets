package com.example.mypets.listpet

import android.util.Patterns
import com.example.mypets.R

open class check_parametr {

    fun password_check(Pasword1: String, Pasword2: String?): Int? {

        if (Pasword1.isEmpty()) {
            return R.string.error_password_name_empty
        }

        if (Pasword2 != null && Pasword1 != Pasword2) {
            return R.string.error_password_match
        }

        if (Pasword1.length < 6) {
            return R.string.password_too_short
        }

        if (Pasword1.none { it.isLetter() }) {
            return R.string.error_password_letter
        }

        if (Pasword1.none { it.isDigit() }) {
            return R.string.error_password_digit
        }

        if (Pasword1.none { it.isUpperCase() }) {
            return R.string.error_password_uppercase
        }

        return null
    }

    fun first_second_name(firstName: String,secondName: String): Int?{
        if (firstName.isBlank()) {
            return R.string.error_first_name_empty
        }

        if (firstName.trim().length < 2) {
            return R.string.error_first_name_length
        }

        if (secondName.isBlank()) {
            return R.string.error_second_name_empty
        }

        if (secondName.trim().length < 2) {
            return R.string.error_second_name_length
        }

        return null
    }
    fun email_check(email: String): Int? {

        if (email.isBlank()) {
            return R.string.enter_email
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            return R.string.error_email
        }

        return null
    }





}