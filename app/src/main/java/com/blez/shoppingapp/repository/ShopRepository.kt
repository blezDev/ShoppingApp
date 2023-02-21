package com.blez.shoppingapp.repository

import com.blez.shoppingapp.data.*
import com.blez.shoppingapp.data.remoteAPI.ShopAPI
import javax.inject.Inject

class ShopRepository @Inject constructor(val shopAPI: ShopAPI) {

    suspend fun loginUser(loginUser: LoginUser)  = shopAPI.LoginUser(loginUser)

    suspend fun signup(loginUser: LoginUser)  = shopAPI.SignupUser(loginUser)

 suspend  fun getItems() = shopAPI.getShopItems()
    suspend fun getSelectedItem(pName : String) = shopAPI.getItemDetail(Pname(pName))

   suspend fun getCartItems(email : String, token : String) = shopAPI.getShoppingCartItems(emailData(email))
    suspend fun createCartItem(createCartItem: CreateCartItem) = shopAPI.createCartItem(createCartItem)
    suspend fun deleteCartItem(_id : String) = shopAPI.deleteCartItem(_id)
    suspend fun updateCartItem(updateCartItem: UpdateCartItem) = shopAPI.updateCartItem(updateCartItem)
    suspend fun deleteALLCartItems(email: String,token: String) = shopAPI.deleteAllCartItems(emailData(email))


}