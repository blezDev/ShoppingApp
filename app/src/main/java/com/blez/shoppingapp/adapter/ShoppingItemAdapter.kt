package com.blez.shoppingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.blez.shoppingapp.R
import com.blez.shoppingapp.data.ShopItem
import com.blez.shoppingapp.databinding.ShopItemBinding
import com.bumptech.glide.Glide

class ShoppingItemAdapter(private val context : Context,private val shopItems : List<ShopItem>) : RecyclerView.Adapter<ShoppingItemAdapter.ItemViewHolder>() {
    private lateinit var binding:ShopItemBinding
    var onItemClick : ((ShopItem)->Unit) ?= null
    var onImgClick : ((ShopItem)->Unit) ?= null
     class ItemViewHolder(binding:ShopItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.shop_item,parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = shopItems[position]
        binding.apply {
            Glide.with(context).load(item.pImage).into(pImage)
            pNameText.text = item.pName
            pCompanyName.text = item.pCompany
            pCost.text = item.pCost
        }
        binding.AddCartBTN.setOnClickListener {
            onItemClick?.invoke(item)
        }
        binding.pImage.setOnClickListener {
            onImgClick?.invoke(item)
        }
        binding.pNameText.setOnClickListener {
            onImgClick?.invoke(item)
        }

    }

    override fun getItemCount(): Int {
       return shopItems.size
    }
}