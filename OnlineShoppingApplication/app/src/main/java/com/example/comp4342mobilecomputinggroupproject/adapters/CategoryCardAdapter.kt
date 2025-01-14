package com.example.comp4342mobilecomputinggroupproject.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.comp4342mobilecomputinggroupproject.R
import com.example.comp4342mobilecomputinggroupproject.data.Category


class CategoryCardAdapter(private val cateList: ArrayList<Category>):
    RecyclerView.Adapter<CategoryCardAdapter.CateViewHolder>() {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_man_card,
            parent, false)
        return CateViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cateList.size
    }

    override fun onBindViewHolder(holder: CateViewHolder, position: Int) {
        val currentItem = cateList[position]
        holder.txtCategoryName.text = currentItem.categoryName
        //holder.btnDelete = currentItem.btnDelete

        // Set click listener for the item view
        holder.btnDelete.setOnClickListener {
            onClickListener?.onClick(position, currentItem)
            println("Clicked button position $position")
            println("$position Category Name: ${holder.txtCategoryName.text.toString()}")
        }
    }

    // Set the click listener for the adapter
    fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
    }

    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, category: Category)
    }

    class CateViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtCategoryName : TextView = itemView.findViewById(R.id.cardCategoryName)
        val btnDelete : Button = itemView.findViewById(R.id.btnDelete)

        /*
        init {
            btnDelete.setOnClickListener{
                val position: Int = adapterPosition
                println("Clicked button position $position")
                println("$position Category Name ${txtCategoryName.text.toString()}")
            }
        }

         */

    }


}