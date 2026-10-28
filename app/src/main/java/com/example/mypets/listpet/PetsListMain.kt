package com.example.mypets.listpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypets.R
import com.example.mypets.databinding.FragmentPetsListMainBinding


class PetsListMain : Fragment(), petListadapter.Listener {

    lateinit var binding: FragmentPetsListMainBinding
    private var adapter = petListadapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentPetsListMainBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        binding.rcviewlistpet.layoutManager = GridLayoutManager(this@PetsListMain,3)

        binding.rcviewlistpet.adapter = adapter



        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PetsListMain()

    }
}