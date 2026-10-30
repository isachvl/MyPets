package com.example.mypets.listpet

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypets.MainViewModel
import com.example.mypets.R
import com.example.mypets.Registration.ViewModelAuth
import com.example.mypets.databinding.FragmentPetsListMainBinding
import com.example.mypets.databinding.HeaderBinding
import com.example.mypets.mainbd
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class PetsListMain : Fragment(), petListadapter.Listener {
    private val repository = pushNewpetIndatabase()
    private val model: viewmodelNewpets by activityViewModels()
    lateinit var binding: FragmentPetsListMainBinding
    private val auth: ViewModelAuth by activityViewModels()

    val uid = FirebaseAuth.getInstance().currentUser?.uid


    private var adapter = petListadapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun oneClick(pet: InfoPet, position: Int) {

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        binding = FragmentPetsListMainBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        val screenWidth = resources.displayMetrics.widthPixels
        val density = resources.displayMetrics.density

        val cardWidth = 180 // Желаемая ширина карточки в dp

        val columns = ((screenWidth / density) / cardWidth)
            .toInt()
            .coerceAtLeast(1)



        binding.rcviewlistpet.layoutManager = GridLayoutManager(requireContext(),columns)

        binding.rcviewlistpet.adapter = adapter



        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val headerView = binding.navigationView.getHeaderView(0)
        val headerBinding = HeaderBinding.bind(headerView)
// Показываем загрузку
        headerBinding.avatarLoading.visibility = View.VISIBLE
        headerBinding.imageavatar.visibility = View.INVISIBLE

        if (uid != null) {

            val db = mainbd.getDb(requireContext())

            viewLifecycleOwner.lifecycleScope.launch {

                db.getDao().getItem(uid).collect { userImage ->

                    if (userImage != null) {

                        val bitmap = BitmapFactory.decodeByteArray(
                            userImage.image,
                            0,
                            userImage.image.size
                        )

                        headerBinding.imageavatar.setImageBitmap(bitmap)
                        auth.image.value = bitmap

                    } else {

                        headerBinding.imageavatar.setImageResource(R.drawable.dog)
                        auth.image.value = null
                    }

                    // Останавливаем загрузку
                    headerBinding.avatarLoading.visibility = View.GONE

                    // Плавно показываем фотографию
                    headerBinding.imageavatar.alpha = 0f
                    headerBinding.imageavatar.visibility = View.VISIBLE

                    headerBinding.imageavatar.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .start()
                }
            }

        } else {

            headerBinding.avatarLoading.visibility = View.GONE
            headerBinding.imageavatar.setImageResource(R.drawable.dog)
            headerBinding.imageavatar.visibility = View.VISIBLE
            auth.image.value = null
        }
        auth.loadUser()

        model.checkDataBase()


        binding.apply {
            auth.user.observe(viewLifecycleOwner) {

                headerBinding.fname.text = it.firstName

                headerBinding.sname.text = it.secondName



            }




        }

        binding.addPets.setOnClickListener {

            findNavController().navigate(R.id.action_petsListMain_to_addNewPet)

        }

        model.pets.observe(viewLifecycleOwner){
            it -> adapter.addPet(it)


        }

        binding.buttonsetting.setOnClickListener{

            findNavController().navigate(R.id.action_petsListMain_to_settinguser)

        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PetsListMain()

    }

}