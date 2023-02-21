package com.blez.shoppingapp.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blez.shoppingapp.data.CreateCartItem
import com.blez.shoppingapp.data.ShopItem
import com.blez.shoppingapp.repository.ShopRepository
import com.blez.shoppingapp.ui.dashboard.DashboardViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val shopRepository: ShopRepository) : ViewModel() {
    sealed class SetupEvent{
        object ShopItemsLoading :  SetupEvent()
        data class ShopItems(val data : List<ShopItem>) : SetupEvent()
        object ShopCartItemCreated : SetupEvent()
    }
    private val _setupEvent = MutableSharedFlow<SetupEvent>()
    val setupEvent : SharedFlow<SetupEvent>
        get() = _setupEvent
    private val _items = MutableStateFlow<SetupEvent>(SetupEvent.ShopItemsLoading)
    val items : StateFlow<SetupEvent> = _items

    fun getItems() {
        _items.value = SetupEvent.ShopItemsLoading
        viewModelScope.launch(Dispatchers.Main) {
             val result = shopRepository.getItems()
            val value = SetupEvent.ShopItems(result)
            _items.value = SetupEvent.ShopItems(result)
        }
    }
    private val _createStatus  = MutableStateFlow<SetupEvent>(SetupEvent.ShopItemsLoading)
    val createStatus : StateFlow<SetupEvent>
    get() = _createStatus

    fun createCartItem(createCartItem: CreateCartItem){
        viewModelScope.launch(Dispatchers.Main) {
            val result = shopRepository.createCartItem(createCartItem)
            if (result.isSuccessful){
                _createStatus.value = SetupEvent.ShopCartItemCreated
            }

        }
    }


}