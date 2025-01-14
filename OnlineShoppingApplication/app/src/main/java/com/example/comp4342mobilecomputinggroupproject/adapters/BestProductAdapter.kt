package com.example.comp4342mobilecomputinggroupproject.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.comp4342mobilecomputinggroupproject.data.Product
import com.example.comp4342mobilecomputinggroupproject.databinding.BestDealsRvItemBinding
import com.example.comp4342mobilecomputinggroupproject.databinding.ProductRvItemBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.interfaces.ProductListAdapter

class BestProductAdapter: RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>(), ProductListAdapter{
    inner class BestProductViewHolder(private val binding: ProductRvItemBinding): ViewHolder(binding.root){
        fun bind(product: Product){
            binding.apply {
                var cHelper = ConnectionHelper
                val currentImageURL: String
                if(product.imageURL.toString().substring(1, 6) == "https"){
                    currentImageURL = product.imageURL?.get(0).toString()
                }else{
                    val imageURL = product.imageURL!!.toString().replace("[", "").replace("]", "")
                    currentImageURL = cHelper.cURL + "/mobileproject/imageDatabase/" + imageURL
                }
                Glide.with(itemView).load(currentImageURL).into(imgProduct)
                product.discount?.let {
                    val priceAfterOffer = product.discount!! * product.sellPrice!!
                    tvNewPrice.text = "$ ${String.format("%.0f", priceAfterOffer)}"
                }
                if(product.discount == 1.toDouble()){
                    tvNewPrice.visibility = View.INVISIBLE
                }else{
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                tvPrice.text = "$ ${product.sellPrice}"
                tvName.text = product.productName
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productID == newItem.productID
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun submitProductList(products: List<Product>) {
        differ.submitList(products)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener{
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null
}