package com.blez.shoppingapp.data

import com.google.gson.annotations.SerializedName

data class LoginUser(
    @SerializedName("email") val email : String,val password : String)
data class emailData(
    @SerializedName("email") val email : String)

data class Pname(
    @SerializedName("pName") val pName : String)


data class LoginStatus(
    val message: String,
    val token: String?
)
data class RegisterStatus(
    val token: String,
    val user: User
)

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val password: String,
    val updatedAt: String
)