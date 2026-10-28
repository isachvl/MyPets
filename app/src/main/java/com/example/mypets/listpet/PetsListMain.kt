package com.example.mypets.listpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypets.R
import com.example.mypets.databinding.FragmentPetsListMainBinding


class PetsListMain : Fragment(), petListadapter.Listener {


    private val model: viewmodelNewpets by activityViewModels()
    lateinit var binding: FragmentPetsListMainBinding
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

        binding.rcviewlistpet.layoutManager = GridLayoutManager(requireContext(),3)

        binding.rcviewlistpet.adapter = adapter

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.addPets.setOnClickListener {

            findNavController().navigate(R.id.action_petsListMain_to_addNewPet)

        }

        model.pets.observe(viewLifecycleOwner){
            it -> adapter.addPet(it)


        }


        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PetsListMain()

    }

}