package com.blez.shoppingapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blez.shoppingapp.data.CreateCartItem
import com.blez.shoppingapp.repository.ShopRepository
import com.blez.shoppingapp.ui.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val shopRepository: ShopRepository) : ViewModel() {
    sealed class SetupEvent{
        object ShopItemsLoading :  SetupEvent()
        data class ShopItem(val data : com.blez.shoppingapp.data.ShopItem) : SetupEvent()
        object ShopCartItemCreated : DetailViewModel.SetupEvent()
    }

    private val _createStatus  = MutableStateFlow<DetailViewModel.SetupEvent>(DetailViewModel.SetupEvent.ShopItemsLoading)
    val createStatus : StateFlow<DetailViewModel.SetupEvent>
        get() = _createStatus

    fun getSelectedItem(pName : String){
        viewModelScope.launch(Dispatchers.Main) {
            val result = shopRepository.getSelectedItem(pName = pName)
            _createStatus.value = SetupEvent.ShopItem(data =result )

        }
    }

    private val _createStatusItem  = MutableStateFlow<DetailViewModel.SetupEvent>(DetailViewModel.SetupEvent.ShopItemsLoading)
    val createStatusItem : StateFlow<DetailViewModel.SetupEvent>
        get() = _createStatusItem

    fun createCartItem(createCartItem: CreateCartItem){
        viewModelScope.launch(Dispatchers.Main) {
            val result = shopRepository.createCartItem(createCartItem)
            if (result.isSuccessful){
                _createStatusItem.value = DetailViewModel.SetupEvent.ShopCartItemCreated
            }

        }
    }
}