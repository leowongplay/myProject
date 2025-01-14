package com.example.comp4342mobilecomputinggroupproject.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.comp4342mobilecomputinggroupproject.adapters.BestProductAdapter
import com.example.comp4342mobilecomputinggroupproject.data.Product
import com.example.comp4342mobilecomputinggroupproject.databinding.FragmentBaseCategoryBinding

class Bags: BaseCategory() {
    private val bagsBestDealList = mutableListOf<Product>()
    private val bagsBestProductList = mutableListOf<Product>()

    val bagsBestDealURL: String = "${URL}bags/bagsBestDeal.php"
    val bagsBestProductURL: String = "${URL}bags/bagsBestProduct.php"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProduct(bagsBestDealURL, offerAdapter, bagsBestDealList)
        getProduct(bagsBestProductURL, bestProductAdapter, bagsBestProductList)
    }
}