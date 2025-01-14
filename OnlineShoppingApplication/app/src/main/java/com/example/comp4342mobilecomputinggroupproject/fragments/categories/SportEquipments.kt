package com.example.comp4342mobilecomputinggroupproject.fragments.categories

import android.os.Bundle
import android.view.View
import com.example.comp4342mobilecomputinggroupproject.data.Product

class SportEquipments: BaseCategory() {
    private val sportEquipmentsBestDealList = mutableListOf<Product>()
    private val sportEquipmentsBestProductList = mutableListOf<Product>()

    val sportEquipmentsBestDealURL: String = "${URL}sportEquipment/sportEquipmentsBestDeal.php"
    val sportEquipmentsBestProductURL: String = "${URL}sportEquipment/sportEquipmentsBestProduct.php"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProduct(sportEquipmentsBestDealURL, offerAdapter, sportEquipmentsBestDealList)
        getProduct(sportEquipmentsBestProductURL, bestProductAdapter, sportEquipmentsBestProductList)
    }
}