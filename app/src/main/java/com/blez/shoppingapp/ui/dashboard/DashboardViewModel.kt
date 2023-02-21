package com.blez.shoppingapp.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blez.shoppingapp.data.ShopCartItem
import com.blez.shoppingapp.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val shopRepository: ShopRepository) : ViewModel() {

    sealed class SetupEvent{
        object ShopCartItemsLoading : SetupEvent()
        data class ShopItems(val data : List<ShopCartItem>) : SetupEvent()
        object ShopCartItemsEmpty : SetupEvent()
        object DeletedConfirmation : SetupEvent()
    }
   private val _cartItems = MutableStateFlow<SetupEvent>(SetupEvent.ShopCartItemsLoading)
    val cartItems : StateFlow<SetupEvent>
    get() = _cartItems

    fun getCartItems(email: String,token : String) {
        _cartItems.value = SetupEvent.ShopCartItemsLoading
        viewModelScope.launch(Dispatchers.Main) {
            val result = shopRepository.getCartItems(email = email, token = token)
            if (result.isEmpty())
                _cartItems.value = SetupEvent.ShopCartItemsEmpty
                else
                 _cartItems.value = SetupEvent.ShopItems(result)
        }
    }

    private val _deleteStatus = MutableStateFlow<SetupEvent>(SetupEvent.ShopCartItemsLoading)
    val deleteStatus : StateFlow<SetupEvent>
    get() = _deleteStatus

    fun deleteAllItems(email: String){
        viewModelScope.launch {

            val status = shopRepository.deleteALLCartItems(email,email)
            status
          if (  status.isSuccessful){
              _deleteStatus.value = SetupEvent.DeletedConfirmation
          }
        }
    }


}