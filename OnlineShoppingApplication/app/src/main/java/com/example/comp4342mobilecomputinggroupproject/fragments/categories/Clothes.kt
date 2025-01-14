package com.example.comp4342mobilecomputinggroupproject.fragments.categories

import android.os.Bundle
import android.view.View
import com.example.comp4342mobilecomputinggroupproject.data.Product

class Clothes: BaseCategory() {
    private val clothesBestDealList = mutableListOf<Product>()
    private val clothesBestProductList = mutableListOf<Product>()

    val clothesBestDealURL: String = "${URL}clothes/clothesBestDeal.php"
    val clothesBestProductURL: String = "${URL}clothes/clothesBestProduct.php"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProduct(clothesBestDealURL, offerAdapter, clothesBestDealList)
        getProduct(clothesBestProductURL, bestProductAdapter, clothesBestProductList)
    }


}