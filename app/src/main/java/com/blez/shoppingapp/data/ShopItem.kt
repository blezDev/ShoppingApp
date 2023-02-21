package com.blez.shoppingapp.data



data class ShopItem(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val pCompany: String,
    val pCost: String,
    val pCount: String,
    val pDescription: String,
    val pImage: String,
    val pName: String,
    val updatedAt: String
)

data class ShopCartItem(
    val __v: Int,
    val _id: String,
    val email: String,
    val pCompany: String,
    val pCost: String,
    val pCount: String,
    val pDescription: String,
    val pImage: String,
    val pName: String
)