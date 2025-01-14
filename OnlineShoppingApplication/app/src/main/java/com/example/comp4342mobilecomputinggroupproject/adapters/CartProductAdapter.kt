package com.example.comp4342mobilecomputinggroupproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.data.CartProduct
import com.example.comp4342mobilecomputinggroupproject.data.ProductKenny
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import kotlin.text.equals


class CartProductCardAdapter(private val prodList: ArrayList<CartProduct>) :
        RecyclerView.Adapter<CartProductCardAdapter.ProdViewHolder>() {
        private var onProductClickListener: OnProductClickListener? = null
    private var onImagePlusClickListener: OnImagePlusClickListener? = null
    private var onImageMinusClickListener: OnImageMinusClickListener? = null


    class ProdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductCartName: TextView = itemView.findViewById(R.id.tvProductCartName)
        val tvProductCartPrice: TextView = itemView.findViewById(R.id.tvProductCartPrice)
        val imagePlus: ImageView = itemView.findViewById(R.id.imagePlus)
        val imageMinus: ImageView = itemView.findViewById(R.id.imageMinus)
        val tvCartProductQuantity: TextView = itemView.findViewById(R.id.tvCartProductQuantity)
        val tvCartProductColor: TextView = itemView.findViewById(R.id.tvColor)
        val tvCartProductSize: TextView = itemView.findViewById(R.id.tvCartProductSize)
        val imageCartProduct: ImageView = itemView.findViewById(R.id.imageCartProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdViewHolder {
        val itemView =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.cart_product_item, parent, false)
        return ProdViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProdViewHolder, position: Int) {
        val cartProduct = prodList[position]
        holder.tvProductCartName.text = cartProduct.product.productName
        holder.tvProductCartPrice.text = "$ ${cartProduct.totalPrice}"
        holder.tvCartProductQuantity.text = cartProduct.quantity.toString()
        holder.tvCartProductSize.text = cartProduct.selectedSize
        holder.tvCartProductColor.text = cartProduct.selectedColor
        if(cartProduct.product.imageURL != null){
            //get server URL
            var cHelper = ConnectionHelper
            //get image path
            val currentImageUrl = ConnectionHelper.cURL + "/mobileproject/imageDatabase/" + cartProduct.product.imageURL
            //debug
            println("Image URL: $currentImageUrl")
            println("$cartProduct.imageURL")

            //if product contain image -> load into imageView
            if(!cartProduct.product.imageURL.equals("null")){
                try {
                    //load into imageView
                    //View, URL, xml layout
                    val currentImageUrl: String =
                        if(cartProduct.product.imageURL.toString().take(5) == "https"){
                            cartProduct.product.imageURL.toString()
                        }
                        else{
                            ConnectionHelper.cURL+"/mobileproject/imageDatabase/" +  cartProduct.product.imageURL.toString()
                        }
                    Glide.with(holder.itemView.context).load(currentImageUrl).into(holder.imageCartProduct)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        holder.itemView.setOnClickListener {
            onProductClickListener?.onClick(position, cartProduct)
            println("Clicked button position $position")
            println("$position Category Name: ${holder.tvProductCartName.text}")
        }
        holder.imagePlus.setOnClickListener {
            onImagePlusClickListener?.onClick(position, cartProduct)
            println("Clicked button position $position")
            println("$position Category Name: ${holder.tvProductCartName.text}")
        }
        holder.imageMinus.setOnClickListener {
            onImageMinusClickListener?.onClick(position, cartProduct)
            println("Clicked button position $position")
            println("$position Category Name: ${holder.tvProductCartName.text}")
        }

    }

    override fun getItemCount(): Int {
        return prodList.size
    }


    fun setOnProductClickListener(listener: OnProductClickListener?) {
        this.onProductClickListener = listener
    }

    // Interface for the click listener
    interface OnProductClickListener {
        fun onClick(position: Int, product: CartProduct)
    }

    fun setOnImagePlusClickListener(listener: OnImagePlusClickListener?) {
        this.onImagePlusClickListener = listener
    }

    // Interface for the click listener
    interface OnImagePlusClickListener {
        fun onClick(position: Int, product: CartProduct)
    }

    fun setOnImageMinusClickListener(listener: OnImageMinusClickListener?) {
        this.onImageMinusClickListener = listener
    }

    // Interface for the click listener
    interface OnImageMinusClickListener {
        fun onClick(position: Int, product: CartProduct)
    }
}
