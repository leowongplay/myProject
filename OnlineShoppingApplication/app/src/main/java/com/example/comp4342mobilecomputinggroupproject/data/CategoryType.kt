package com.example.comp4342mobilecomputinggroupproject.data

sealed class CategoryType(val category: String) {
    object Shoes: CategoryType("Shoes")
    object Clothes: CategoryType("Clothes")
    object Bags: CategoryType("Bags")
    object Hats: CategoryType("Hats")
    object SportEquipments: CategoryType("Sport Equipments")
}

