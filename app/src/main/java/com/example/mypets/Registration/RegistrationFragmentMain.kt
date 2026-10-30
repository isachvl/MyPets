package com.example.mypets.Registration

import com.example.mypets.R
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.mypets.databinding.FragmentRegistrationMainBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragmentMain : Fragment() {
    private lateinit var binding: FragmentRegistrationMainBinding
    private val auth = FirebaseAuth.getInstance()
    private val model: ViewModelAuth by viewModels()


        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentRegistrationMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ViewModelSeek()
        binding.apply {
            regButton.setOnClickListener {

                if (model.mode.value == AuthMode.LOGIN){
                    model.showREgistration()
                }
                else{
                    model.showLogin()
                }
            }
            singinButton.setOnClickListener {

                if (validateFields()){
                    val user = AuthUser(
                        firstName = firstNametext.text.toString(),
                        secondName = secondNametext.text.toString(),
                        email = logintext.text.toString().trim(),
                        password = passwordText.text.toString()
                    )

                    showLoading()

                    model.authrepos(user){
                        success, error ->

                        if (success) {
                            hideLoading()
                            SuccessRegOpenFrag()
                        }
                        else if (error != null) {
                            hideLoading()
                            binding.textError.visibility = View.VISIBLE

                            binding.textError.text= getString(error)
                        }
                    }
                }



            }
        }
    }

    companion object {
        @JvmStatic fun newInstance( ) =
                RegistrationFragmentMain()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    private fun SuccessRegOpenFrag(){

        findNavController().navigate(
            R.id.action_registrationFragmentMain_to_petsListMain,
            null,
            navOptions {
                popUpTo(R.id.registrationFragmentMain) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        )

    }

    private fun ViewModelSeek()
    {
        model.mode.observe(viewLifecycleOwner){
            mode ->
            binding.apply {
                val visibility = if (mode == AuthMode.REGISTRATION) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                firstNametext.visibility = visibility
                secondNametext.visibility = visibility
                secondPassword.visibility = visibility

                if (visibility==View.VISIBLE) {
                    regButton.text = getString(R.string.singIn)
                    singinButton.text = getString(R.string.register)

                }
                if (visibility==View.GONE) {
                    regButton.text = getString(R.string.registration)
                    singinButton.text = getString(R.string.singIn)

                }


            }
        }

    }
    private fun validateFields(): Boolean {

        // Получаем текущий режим из ViewModel
        val registration = model.mode.value == AuthMode.REGISTRATION

        val email = binding.logintext.text.toString().trim()
        val password = binding.passwordText.text.toString()
        val firstName = binding.firstNametext.text.toString()
        val secondName = binding.secondNametext.text.toString()
        val repeatPassword = binding.secondPassword.text.toString()

        val errors = mutableMapOf<android.widget.TextView, Int?>(
            binding.logintext to AuthError.email(email),
            binding.passwordText to AuthError.password(password, registration)
        )

        if (registration) {
            errors[binding.firstNametext] = AuthError.name(firstName, true)
            errors[binding.secondNametext] = AuthError.name(secondName, false)
            errors[binding.secondPassword] = AuthError.repeat(repeatPassword, password)
        }

        errors.forEach { (field, error) ->
            field.error = error?.let { getString(it) }
        }

        return errors.values.all { it == null }
    }
    private fun showLoading() {
        binding.loadingOverlay?.visibility = View.VISIBLE
        binding.loadingAuth?.playAnimation()
    }

    private fun hideLoading() {
        binding.loadingAuth?.cancelAnimation()
        binding.loadingOverlay?.visibility = View.GONE
    }


}