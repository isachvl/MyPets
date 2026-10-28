package com.example.mypets.Registration
import com.example.mypets.R
import android.util.Patterns

object AuthError {

    fun email(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches())
            R.string.error_email
        else null

    fun password(value: String, registration: Boolean): Int? = when {
        value.isEmpty() -> R.string.error_password_name_empty
        !registration -> null
        value.length < 8 -> R.string.error_password_length
        value.none { it.isDigit() } -> R.string.error_password_digit
        value.none { it.isUpperCase() } -> R.string.error_password_uppercase
        else -> null
    }

    fun name(value: String, first: Boolean): Int? = when {
        value.isBlank() -> if (first)
            R.string.error_first_name_empty
        else
            R.string.error_second_name_empty

        value.trim().length < 2 -> if (first)
            R.string.error_first_name_length
        else
            R.string.error_second_name_length

        else -> null
    }

    fun repeat(value: String, password: String): Int? =
        if (value != password) R.string.error_password_match else null
}