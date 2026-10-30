package com.example.mypets.listpet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.mypets.R

class takePhoto(
    private val fragment: Fragment,
    private val onResult: (Bitmap) -> Unit
) {

    private val cameraLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            onResult(bitmap)
        }
    }

    private val galleryLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val bitmap = fragment.requireContext().contentResolver
                .openInputStream(uri)
                ?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }

            if (bitmap != null) {
                onResult(bitmap)
            }
        }
    }

    fun addphoto() {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle(R.string.photo_title)
            .setItems(
                arrayOf(
                    fragment.getString(R.string.photo_camera),
                    fragment.getString(R.string.photo_gallery)
                )
            ) { _, which ->
                when (which) {
                    0 -> cameraLauncher.launch(null)
                    1 -> galleryLauncher.launch("image/*")
                }
            }
            .show()
    }
}