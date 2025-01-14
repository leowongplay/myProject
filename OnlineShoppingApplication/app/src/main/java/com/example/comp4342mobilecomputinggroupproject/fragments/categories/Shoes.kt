package com.example.comp4342mobilecomputinggroupproject.fragments.categories

import android.os.Bundle
import android.view.View
import com.example.comp4342mobilecomputinggroupproject.data.Product

class Shoes: BaseCategory() {
    private val shoesBestDealList = mutableListOf<Product>()
    private val shoesBestProductList = mutableListOf<Product>()

    private val shoesBestDealURL: String = "${URL}shoes/shoesBestDeal.php"
    private val shoesBestProductURL: String = "${URL}shoes/shoesBestProducts.php"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProduct(shoesBestDealURL, offerAdapter, shoesBestDealList)
        getProduct(shoesBestProductURL, bestProductAdapter, shoesBestProductList)
    }
}