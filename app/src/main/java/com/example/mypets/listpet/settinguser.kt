package com.example.mypets.listpet

import android.R.attr.fragment
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.example.mypets.R
import com.example.mypets.Registration.AuthError
import com.example.mypets.Registration.AuthMode
import com.example.mypets.Registration.AuthUser
import com.example.mypets.Registration.ViewModelAuth
import com.example.mypets.databinding.FragmentPetsListMainBinding
import com.example.mypets.databinding.FragmentSettinguserBinding
import com.example.mypets.mainbd
import com.example.mypets.user_imagebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.Int

class settinguser : Fragment() {
    lateinit var binding: FragmentSettinguserBinding

    private var selectedImage: Bitmap? = null
    private val check = check_parametr()
    private var redactor: Boolean = false
    private var passwordredactor: Boolean = false
    private val photoPicker = takePhoto(this) { bitmap ->
        selectedImage = bitmap
        binding.imageuser.setImageBitmap(bitmap)
        auth.image.value = selectedImage
        saveImage(bitmap)
    }

    private val auth: ViewModelAuth by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentSettinguserBinding.inflate(inflater,container,false)
        clickaddimage()
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        exitacc()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {



                override fun handleOnBackPressed() {

                    if (!redactor && !passwordredactor) {

                        findNavController().popBackStack()

                    } else {

                        AlertDialog.Builder(requireContext())
                            .setTitle(R.string.sure_exit)
                            .setPositiveButton(R.string.yes) { _, _ ->

                                findNavController().popBackStack()

                            }
                            .setNegativeButton(R.string.no) { _, _ ->

                            }
                            .show()
                    }
                }
            }
        )
        binding.backbuttooon.setOnClickListener{

            if (redactor == false && passwordredactor == false) {
                findNavController().navigate(R.id.action_settinguser_to_petsListMain)
            }
            else{
                AlertDialog.Builder(requireContext()).setTitle(R.string.sure_exit)
                    .setPositiveButton(R.string.yes){ _,_ ->
                        findNavController().navigate(R.id.action_settinguser_to_petsListMain)

                    }
                    .setNegativeButton(R.string.no){ _,_ ->


                    }.show()
            }
        }



        clickaddimage()
        auth.loadUser()
        auth.user.observe(viewLifecycleOwner){
            binding.apply{
                if (auth.image.value!=null) {
                    imageuser.setImageBitmap(auth.image.value)
                }
                else imageuser.setImageResource(R.drawable.baseline_tag_faces_24)
                textname.text = it.secondName
                textemail.text = it.email
                textfname.text = it.firstName
                textname.visibility = View.VISIBLE
                textemail.visibility = View.VISIBLE
                textfname.visibility = View.VISIBLE
                firstname.visibility = View.GONE
                secondnametext.visibility = View.GONE
                emailtext.visibility = View.GONE
                passwordnew.visibility = View.GONE
                passwordedittext.visibility = View.GONE
                passwordnew2.visibility = View.GONE
                passwordsetting.visibility = View.VISIBLE
                editbutton.visibility = View.VISIBLE
            }

        }
        textlistener()
        passwordchange()



    }


    fun passwordchange(){
        binding.passwordsetting.setOnClickListener {
            if (passwordredactor == false) {
                binding.apply {


                    textname.visibility = View.GONE
                    textemail.visibility = View.GONE
                    textfname.visibility = View.GONE
                    emailtext.visibility  = View.VISIBLE
                    passwordnew.visibility = View.VISIBLE
                    passwordedittext.visibility = View.VISIBLE
                    passwordnew2.visibility = View.VISIBLE
                    editbutton.visibility = View.GONE


                }
                passwordredactor = true
            }
            else{
                binding.apply {

                    if (check.password_check(passwordedittext.text.toString(),null)==null &&
                        check.password_check(passwordnew.text.toString(), passwordnew2.text.toString())==null
                        &&  check.email_check(emailtext.text.toString())==null) {


                        AlertDialog.Builder(requireContext()).setTitle(R.string.save_parameters)
                            .setPositiveButton(R.string.yes) { _, _ ->
                                auth.reauthenticate(passwordedittext.text.toString()) { success, error ->
                                    if (success)

                                    {

                                        auth.changePasswordr(passwordnew.text.toString()){
                                            success, message ->

                                            if (success) {

                                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                                                passwordredactor = false

                                                emailtext.visibility = View.GONE
                                                textname.visibility = View.VISIBLE
                                                textemail.visibility = View.VISIBLE
                                                textfname.visibility = View.VISIBLE
                                                passwordnew.visibility = View.GONE
                                                passwordedittext.visibility = View.GONE
                                                passwordnew2.visibility = View.GONE
                                                editbutton.visibility = View.VISIBLE
                                                passwordredactor = false

                                            } else {
                                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                                            }

                                        }

                                    }

                                    else if (error != null) {
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

                                    }

                                }

                            }
                            .setNegativeButton(R.string.no) { _, _ ->
                            }.show()
                    }

                }


            }
        }


    }
    fun exitacc() {

        binding.apply {

            exitAcc.setOnClickListener {

                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.exit_account_title)
                    .setMessage(R.string.exit_account_message)
                    .setPositiveButton(R.string.yes) { _, _ ->

                        FirebaseAuth.getInstance().signOut()

                        auth.image.value = null

                        findNavController().navigate(
                            R.id.registrationFragmentMain
                        )
                    }
                    .setNegativeButton(R.string.no) { _, _ ->

                    }
                    .show()
            }
        }
    }
    fun textlistener() {

        binding.apply {

            editbutton.setOnClickListener {

                if (!redactor) {

                    // Заполняем поля текущими данными
                    firstname.setText(textfname.text)
                    secondnametext.setText(textname.text)
                    emailtext.setText(textemail.text)
                    editbutton.setText(R.string.save_setting)

                    textname.visibility = View.GONE
                    textemail.visibility = View.GONE
                    textfname.visibility = View.GONE
                    passwordsetting.visibility= View.GONE

                    firstname.visibility = View.VISIBLE
                    secondnametext.visibility = View.VISIBLE
                    emailtext.visibility = View.VISIBLE

                    redactor = true

                } else {

                    val firstName = firstname.text.toString().trim()
                    val secondName = secondnametext.text.toString().trim()
                    val email = emailtext.text.toString().trim()

                    val nameError = check.first_second_name(firstName, secondName)
                    val emailError = check.email_check(email)

                    // Проверка имени и фамилии
                    if (nameError != null) {

                        when (nameError) {

                            R.string.error_first_name_empty,
                            R.string.error_first_name_length -> {
                                firstname.error = getString(nameError)
                            }

                            R.string.error_second_name_empty,
                            R.string.error_second_name_length -> {
                                secondnametext.error = getString(nameError)
                            }
                        }
                    }

                    // Проверка электронной почты
                    if (emailError != null) {
                        emailtext.error = getString(emailError)
                    }

                    // Если ошибок нет, сохраняем данные
                    if (nameError == null && emailError == null) {

                        val user = AuthUser(
                            firstName = firstName,
                            secondName = secondName,
                            email = email,
                            password = ""
                        )

                        AlertDialog.Builder(requireContext())
                            .setTitle(R.string.save_parameters)
                            .setPositiveButton(R.string.yes) { _, _ ->

                                viewLifecycleOwner.lifecycleScope.launch {

                                    val result = auth.updateUser(user)

                                    if (
                                        result == R.string.user_updated ||
                                        result == R.string.user_confirm_email
                                    ) {

                                        Toast.makeText(
                                            requireContext(),
                                            result,
                                            Toast.LENGTH_LONG
                                        ).show()

                                        // Выходим из режима редактирования
                                        textname.visibility = View.VISIBLE
                                        textemail.visibility = View.VISIBLE
                                        textfname.visibility = View.VISIBLE
                                        passwordsetting.visibility= View.VISIBLE
                                        firstname.visibility = View.GONE
                                        secondnametext.visibility = View.GONE
                                        emailtext.visibility = View.GONE

                                        redactor = false

                                        // Загружаем обновлённые данные
                                        auth.loadUser()

                                    } else {

                                        Toast.makeText(
                                            requireContext(),
                                            result,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                            .setNegativeButton(R.string.no) { _, _ ->

                            }
                            .show()
                    }
                }
            }
        }
    }

    fun saveImage(bitmap: Bitmap) {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)

        val imageBytes = stream.toByteArray()

        val db = mainbd.getDb(requireContext())

        val item1 = user_imagebase(
            userId = uid,
            image = imageBytes
        )

        Thread {
            db.getDao().insertItem(item1)
        }.start()
    }

    fun clickaddimage(){
        binding.addimage.setOnClickListener {
            photoPicker.addphoto()



        }

    }



    companion object {

        @JvmStatic
        fun newInstance( ) =
            settinguser()
    }

}