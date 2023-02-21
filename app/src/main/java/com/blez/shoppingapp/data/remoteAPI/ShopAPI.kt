package com.blez.shoppingapp.data.remoteAPI

import com.blez.shoppingapp.data.*
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ShopAPI {

    @POST("/users/signin")
    suspend fun LoginUser(@Body loginUser: LoginUser ) : Response<LoginStatus>
    @POST("/users/signup")
    suspend fun SignupUser(@Body loginUser: LoginUser) : Response<RegisterStatus>

    @GET("/shop/items")
   suspend fun getShopItems() : List<ShopItem>
   @POST("/shop/selectItem")
   suspend fun getItemDetail(@Body pname: Pname) : ShopItem


    @POST("/shop/cartItems")
   suspend fun getShoppingCartItems(@Body emailData: emailData ) : List<ShopCartItem>

    @POST("/shop/createCart")
    suspend fun createCartItem(@Body createCartItem: CreateCartItem) : Response<status>

    @POST("/shop/deleteCartItem")
    suspend fun deleteCartItem(@Body _id: String) : Response<status>

    @POST("/shop/updateCart")
    suspend fun updateCartItem(@Body updateCartItem: UpdateCartItem) : Response<status>

    @POST("/shop/deleteAllCart")
    suspend fun deleteAllCartItems(@Body emailData: emailData) : Response<status>


}