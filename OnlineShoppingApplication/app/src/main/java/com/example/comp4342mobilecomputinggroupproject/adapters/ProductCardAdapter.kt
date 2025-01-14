package com.example.comp4342mobilecomputinggroupproject.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Executors
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.data.ProductKenny
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import java.io.FileNotFoundException
import java.net.URL


class ProductCardAdapter(private val prodList: ArrayList<ProductKenny>):
    RecyclerView.Adapter<ProductCardAdapter.ProdViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class ProdViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtProductName : TextView = itemView.findViewById(R.id.txtProductName)
        val txtCategory : TextView = itemView.findViewById(R.id.txtCategory)
        val txtColor : TextView = itemView.findViewById(R.id.txtColor)
        val txtSize : TextView = itemView.findViewById(R.id.txtSize)
        val btnEdit : Button = itemView.findViewById(R.id.btnEdit)
        val imageProduct : ImageView = itemView.findViewById(R.id.imageProduct)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_man_card,
            parent, false)
        return ProdViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return prodList.size
    }

    override fun onBindViewHolder(holder: ProdViewHolder, position: Int) {
        val currentItem = prodList[position]

        holder.txtProductName.text = currentItem.productName
        holder.txtCategory.text = currentItem.categoryName
        holder.txtColor.text = currentItem.color
        holder.txtSize.text = currentItem.size

        if(currentItem.imageURL != null){
            //get server URL
            var cHelper = ConnectionHelper

            //get image path
            val currentImageUrl: String = if(currentItem.imageURL.toString().take(5) == "https"){
                currentItem.imageURL.toString()
            }else{
                cHelper.cURL + "/mobileproject/imageDatabase/" + currentItem.imageURL
            }

            //debug
            println("Image URL: $currentImageUrl")
            println("$currentItem.imageURL")

            //if product contain image -> load into imageView
            if(!currentItem.imageURL.equals("null")){
                try {
                    //load into imageView
                    //View, URL, xml layout
                    Glide.with(holder.itemView.context).load(currentImageUrl).into(holder.imageProduct)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        // Set click listener for the item view
        holder.btnEdit.setOnClickListener {
            onClickListener?.onClick(position, currentItem)
        }

    }

    // Set the click listener for the adapter
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, product: ProductKenny)
    }

}