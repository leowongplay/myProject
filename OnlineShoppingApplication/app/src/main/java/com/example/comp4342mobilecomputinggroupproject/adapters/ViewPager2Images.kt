package com.example.comp4342mobilecomputinggroupproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.comp4342mobilecomputinggroupproject.databinding.ViewpagerImagesItemBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper

class ViewPager2Images : RecyclerView.Adapter<ViewPager2Images.ViewPager2ImagesViewHolder>(){

    inner class ViewPager2ImagesViewHolder(val binding: ViewpagerImagesItemBinding) :
        ViewHolder(binding.root){
            fun bind(imagePath : String){
                var cHelper = ConnectionHelper
                val currentImageURL: String
                if(imagePath.substring(0, 5) == "https"){
                    currentImageURL = imagePath
                }else{
                    val imageURL = imagePath.replace("[", "").replace("]", "")
                    currentImageURL = cHelper.cURL + "/mobileproject/imageDatabase/" + imageURL
                }
                Glide.with(itemView).load(currentImageURL).into(binding.imageProductDetails)
            }
        }

    private val diffCallback = object : DiffUtil.ItemCallback<String>(){
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ImagesViewHolder {
        return ViewPager2ImagesViewHolder(
            ViewpagerImagesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewPager2ImagesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}