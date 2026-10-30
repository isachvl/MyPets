package com.example.mypets.listpet


import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.graphics.BitmapFactory
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypets.R
import com.example.mypets.Registration.ViewModelAuth
import com.example.mypets.databinding.AddNewpetsBinding
import com.example.mypets.databinding.FragmentPetsListMainBinding
import androidx.core.widget.doAfterTextChanged
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class addNewPet : Fragment() {
    private var selectedImage: Bitmap? = null
    private val photoPicker = takePhoto(this) { bitmap ->
        selectedImage = bitmap
        binding.imagepet.setImageBitmap(bitmap)
    }

    lateinit var binding: AddNewpetsBinding
    private val model: viewmodelNewpets by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddNewpetsBinding.inflate(inflater,container,false)


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        takepets()
        clickaddimage()
        binding.datepet.doAfterTextChanged { editable ->

            val text = editable.toString()
            val digits = text.filter { it.isDigit() }.take(8)

            val formatted = buildString {
                digits.forEachIndexed { index, char ->
                    if (index == 2 || index == 4) append(".")
                    append(char)
                }
            }

            if (text != formatted) {
                binding.datepet.setText(formatted)
                binding.datepet.setSelection(formatted.length)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PetsListMain()

    }

    private fun takepets(){


        binding.apply {

            addpet.setOnClickListener {
                val birthDate = datepet.text.toString().trim()

                if (!isValidDate(birthDate)) {
                    datepet.error = "Введите корректную дату (возраст до 30 лет)"
                    return@setOnClickListener
                }

                if (nameedittext.text.toString().trim().isEmpty()) {
                    nameedittext.error = "Введите кличку"
                    return@setOnClickListener
                }

                if (datepet.text.toString().trim().isEmpty()) {
                    datepet.error = "Введите дату рождения"
                    return@setOnClickListener
                }

                if (typePet.text.toString().trim().isEmpty()) {
                    typePet.error = "Введите вид животного"
                    return@setOnClickListener
                }


                val pet = InfoPet(
                    name = nameedittext.text.toString().trim(),
                    dateBirth = birthDate,
                    typePet = typePet.text.toString().trim(),
                    breed = breedPet.text.toString().trim(),
                    gender = when (binding.petGenderGroup.checkedRadioButtonId) {
                        R.id.radioMale -> "male"
                        R.id.radioFemale -> "female"
                        else -> "Not male"},
                    image = selectedImage


                )
                model.addPet(pet)
                findNavController().navigate(R.id.action_addNewPet_to_petsListMain)
            }

        }


    }
    fun clickaddimage(){


        binding.addimage.setOnClickListener {
            photoPicker.addphoto()
        }


    }
    private fun isValidDate(value: String): Boolean {

        // Проверяем формат ДД.ММ.ГГГГ
        if (!Regex("""\d{2}\.\d{2}\.\d{4}""").matches(value)) {
            return false
        }

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
        formatter.isLenient = false

        val date = try {
            formatter.parse(value) ?: return false
        } catch (e: Exception) {
            return false
        }

        // Сегодняшняя дата без времени
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Максимальный возраст питомца — 30 лет
        val minDate = (today.clone() as Calendar).apply {
            add(Calendar.YEAR, -30)
        }

        return !date.after(today.time) && !date.before(minDate.time)
    }
}