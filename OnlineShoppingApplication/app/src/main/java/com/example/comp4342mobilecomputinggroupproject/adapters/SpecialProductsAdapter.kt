package com.example.comp4342mobilecomputinggroupproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.comp4342mobilecomputinggroupproject.data.Product
import com.example.comp4342mobilecomputinggroupproject.databinding.SpecialRvItemBinding
import com.example.comp4342mobilecomputinggroupproject.helper.ConnectionHelper
import com.example.comp4342mobilecomputinggroupproject.interfaces.ProductListAdapter

class SpeicalProductsAdapter: RecyclerView.Adapter<SpeicalProductsAdapter.SpecialProductsViewHolder>(), ProductListAdapter{
    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root){
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
                Glide.with(itemView).load(currentImageURL).into(imageSpecialRvItem)
                tvSpecialProductName.text = product.productName
                tvSpecialPrdouctPrice.text = product.sellPrice.toString()
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productName == newItem.productName
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun submitProductList(products: List<Product>) {
        differ.submitList(products)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
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