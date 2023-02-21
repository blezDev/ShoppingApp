package com.blez.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blez.shoppingapp.R
import com.blez.shoppingapp.data.ShopCartItem
import com.blez.shoppingapp.databinding.CartItemBinding
import com.bumptech.glide.Glide

class ShoppingCartAdapter(private val context : Context,private var shopCart : List<ShopCartItem>) : RecyclerView.Adapter<ShoppingCartAdapter.ItemViewHolder>() {
    private lateinit var binding : CartItemBinding
   inner class ItemViewHolder(binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.cart_item,parent,false)
     return   ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = shopCart[position]
        Glide.with(context).load(item.pImage).into(binding.pImage)
        binding.pName.text = item.pName
        binding.pCompanyName.text = item.pCompany
        binding.pCost.text = item.pCost
        binding.pCount.text = "Quantity :${item.pCount} "

    }

    override fun getItemCount(): Int {
        return shopCart.size
    }
}