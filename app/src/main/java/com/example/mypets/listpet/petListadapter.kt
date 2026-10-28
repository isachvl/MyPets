package com.example.mypets.listpet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypets.R

import com.example.mypets.databinding.OnepetcartBinding

class petListadapter(val listener: Listener): RecyclerView.Adapter<petListadapter.Petholder>() {

    var petlist: List<InfoPet> = emptyList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): petListadapter.Petholder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.onepetcart,parent,false)
        return Petholder(view)
    }

    override fun onBindViewHolder(holder: petListadapter.Petholder, position: Int) {

        holder.bind(petlist[position], listener,position)

    }

    override fun getItemCount(): Int {
        return petlist.size
    }
    class Petholder(item: View): RecyclerView.ViewHolder(item){
        val binding = OnepetcartBinding.bind(item)

        fun bind(pet: InfoPet, listener: Listener, position: Int) = with(binding){


            binding.apply{
                petimage.setImageBitmap(pet.image)

                namepet.text = pet.name

                petinfo.text = pet.typePet




            }


        }

    }
    fun addPet(pet: List<InfoPet>){
        petlist = pet
        notifyDataSetChanged()
    }
    interface Listener{

        fun oneClick(pet: InfoPet,position: Int)

    }

}