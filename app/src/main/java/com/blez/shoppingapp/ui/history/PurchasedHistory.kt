package com.blez.shoppingapp.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blez.shoppingapp.R
import com.blez.shoppingapp.adapter.ShoppingCartAdapter
import com.blez.shoppingapp.databinding.FragmentPurchasedHistoryBinding
import com.blez.shoppingapp.util.TokenManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchasedHistory : Fragment() {
    private lateinit var binding : FragmentPurchasedHistoryBinding
    private lateinit var tokenManager: TokenManager
    private val historyViewModel : HistroyViewModel by viewModels()
    private lateinit var shoppingCartAdapter : ShoppingCartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_purchased_history, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tokenManager = TokenManager(requireContext())
        historyViewModel.getHistory(tokenManager.getEmail()!!)
        observeUI()
    }

    private fun observeUI()
    {
       lifecycleScope.launchWhenCreated {
           historyViewModel.item.collect{events->
               when(events){
                   is HistroyViewModel.SetupEvent.ShopItemsLoading->{
                       binding.progressBar4.isVisible = true
                       binding.emptycartImg.isVisible = false
                   }
                   is HistroyViewModel.SetupEvent.ShopItems->{
                       shoppingCartAdapter = ShoppingCartAdapter(requireContext(),events.data)
                       setupRecyclerview()
                       binding.progressBar4.isVisible = false
                       binding.emptycartImg.isVisible = false

                   }
                   is HistroyViewModel.SetupEvent.ShopCartItemsEmpty->{
                       binding.progressBar4.isVisible = false
                       binding.emptycartImg.isVisible = true
                   }
                   else->Unit

               }

           }
       }
    }
    private fun setupRecyclerview(){
        binding.recyclerView2.adapter = shoppingCartAdapter
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
    }
}