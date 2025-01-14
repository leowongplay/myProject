package com.example.comp4342mobilecomputinggroupproject.data

data class ProductKenny(
    var productID: Int,
    var categoryID: Int? = null,
    var categoryName: String? = null,
    var description: String? = null,
    var productName: String,
    var sellPrice: Double,
    var discount: Double? = null,
    var qty: Int,
    var imageURL: String? = null,
    var color: String? = null,
    var size: String? = null,
    var useStatus: Int
)

