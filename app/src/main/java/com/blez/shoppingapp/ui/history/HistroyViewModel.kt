package com.blez.shoppingapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blez.shoppingapp.data.ShopCartItem
import com.blez.shoppingapp.data.ShopItem
import com.blez.shoppingapp.repository.ShopRepository
import com.blez.shoppingapp.ui.dashboard.DashboardViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistroyViewModel @Inject constructor(val shopRepository: ShopRepository) : ViewModel() {
    sealed class SetupEvent{
        object ShopItemsLoading :  SetupEvent()
        data class ShopItems(val data : List<ShopCartItem>) : SetupEvent()
        object ShopCartItemCreated : SetupEvent()
        object ShopCartItemsEmpty : SetupEvent()
    }

    private val _item = MutableStateFlow<HistroyViewModel.SetupEvent>(HistroyViewModel.SetupEvent.ShopItemsLoading)
    val item : StateFlow<HistroyViewModel.SetupEvent>
        get() = _item

    fun getHistory(email : String){
        viewModelScope.launch(Dispatchers.Main) {
            val result  =    shopRepository.history(email)
            if (result.isEmpty()){
                _item.value = SetupEvent.ShopCartItemsEmpty
            }
            else
                _item.value = SetupEvent.ShopItems(result)

        }

    }


}