package com.example.comp4342mobilecomputinggroupproject.fragments.categories

import android.os.Bundle
import android.view.View
import com.example.comp4342mobilecomputinggroupproject.data.Product

class Hats: BaseCategory() {
    private val hatsBestDealList = mutableListOf<Product>()
    private val hatsBestProductList = mutableListOf<Product>()

    val hatsBestDealURL: String = "${URL}hats/hatsBestDeal.php"
    val hatsBestProductURL: String = "${URL}hats/hatsBestProduct.php"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProduct(hatsBestDealURL, offerAdapter, hatsBestDealList)
        getProduct(hatsBestProductURL, bestProductAdapter, hatsBestProductList)
    }
}