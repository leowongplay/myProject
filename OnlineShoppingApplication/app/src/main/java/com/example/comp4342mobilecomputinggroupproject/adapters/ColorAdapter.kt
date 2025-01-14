package com.example.comp4342mobilecomputinggroupproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.comp4342mobilecomputinggroupproject.databinding.ColorRvItemBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper

class ColorAdapter : RecyclerView.Adapter<ColorAdapter.ColorsViewHolder>(){
    private var selectedPosition = -1

    inner class ColorsViewHolder(private val binding: ColorRvItemBinding) : ViewHolder(binding.root){
        fun bind(imageURL: String, position: Int) {
            var cHelper = ConnectionHelper
            val currentImageURL: String
            if(imageURL.substring(0, 5) == "https"){
                currentImageURL = imageURL
            }else{
                currentImageURL = cHelper.cURL + "/mobileproject/imageDatabase/" + imageURL
            }
            Glide.with(itemView).load(currentImageURL).into(binding.tvColor)
            if (position == selectedPosition){
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    tvColorSelected.visibility = View.VISIBLE
                    tvColor.visibility = View.INVISIBLE
                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    tvColorSelected.visibility = View.INVISIBLE
                    tvColor.visibility = View.VISIBLE
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        return ColorsViewHolder(
            ColorRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        val imageURL = differ.currentList[position]
        holder.bind(imageURL, position)

        holder.itemView.setOnClickListener{
            if(selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(imageURL)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onItemClick: ((String) -> Unit)? = null
}