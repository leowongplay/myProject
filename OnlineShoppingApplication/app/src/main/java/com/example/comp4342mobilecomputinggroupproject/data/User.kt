package com.example.mobileproject.data

data class User(
    var fullName: String,
    var phone: String,
    var email: String,
    var address: String,
    var username: String,
    var imagePath: String = ""
)
