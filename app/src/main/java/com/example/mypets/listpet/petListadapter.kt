package com.example.mypets.listpet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypets.R

import com.example.mypets.databinding.OnepetcartBinding

class petListadapter(val listener: Listener): RecyclerView.Adapter<petListadapter.Petholder>() {

    val petlist = ArrayList<pet>()
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

        fun bind(pet: pet, listener: Listener, position: Int) = with(binding){


            binding.apply{
                petimage.setImageResource(pet.imageID)

                namepet.text = pet.Info.toString()

                petinfo.text = pet.title.toString()


            }


        }

    }
    interface Listener{

        fun oneClick(pet: pet,position: Int)

    }

}