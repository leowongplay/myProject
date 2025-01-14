package com.example.comp4342mobilecomputinggroupproject.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    var productID: Int,
    var categoryID : Int? = null,
    var productName: String? = null,
    var color: MutableList<String>? = null,
    var size: MutableList<String>? = null,
    var description : String? = null,
    var sellPrice: Double? = null,
    var discount: Double? = null,
    var imageURL: MutableList<String>? = null
) : Parcelable {
    constructor() : this(0)
}

