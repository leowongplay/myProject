package com.example.comp4342mobilecomputinggroupproject.data

data class CartProduct(
    val product: ProductKenny,
    val quantity: Int,
    val totalPrice: Double,
    val selectedColor: String,
    val selectedSize: String
)

