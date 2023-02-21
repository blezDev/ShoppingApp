package com.blez.shoppingapp.data

data class CreateCartItem(
    val email: String,
    val pCompany: String,
    val pCost: String,
    val pCount: String,
    val pDescription: String,
    val pImage: String,
    val pName: String,
    val token: String
)
data class UpdateCartItem(
    val email: String,
    val pCompany: String,
    val pCost: String,
    val pCount: String,
    val pDescription: String,
    val pImage: String,
    val pName: String,
    val _id : String,
    val token: String
)
data class status(
    val message: String
)