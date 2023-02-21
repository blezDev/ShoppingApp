package com.blez.shoppingapp.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blez.shoppingapp.R
import com.blez.shoppingapp.adapter.ShoppingCartAdapter
import com.blez.shoppingapp.data.CreateCartItem
import com.blez.shoppingapp.data.ShopCartItem
import com.blez.shoppingapp.databinding.FragmentDashboardBinding
import com.blez.shoppingapp.util.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private val dashboardViewModel : DashboardViewModel by viewModels()

    private var _binding: FragmentDashboardBinding? = null
    private var shoppingCartAdapter : ShoppingCartAdapter?= null
    private lateinit var tokenManager: TokenManager
    private lateinit var item : List<ShopCartItem>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tokenManager = TokenManager(requireContext())
        subscribeToObserver()
        dashboardViewModel.getCartItems(tokenManager.getEmail()!!,token = tokenManager.getToken()!!)
        binding.buyBTN.setOnClickListener {
           for (i in item){
               dashboardViewModel.createPurchased(i)
           }
            observeHistory()
        }


    }
    private fun setupRecyclerview(){
        binding.recyclerView.adapter = shoppingCartAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun observeHistory(){
       lifecycleScope.launch{
           dashboardViewModel.historyStatus.collect{events->
               when(events){
                   is DashboardViewModel.SetupEvent.ShopCartItemsLoading->{

                   }
                   is DashboardViewModel.SetupEvent.CreatedItem->{
                       dashboardViewModel.deleteAllItems(tokenManager.getEmail()!!)
                       observeBuy()
                   }
                   else->Unit
               }

           }
       }

    }



    private fun subscribeToObserver() = lifecycleScope.launchWhenCreated {
        dashboardViewModel.cartItems.collect{events->
            when(events) {
                is DashboardViewModel.SetupEvent.ShopCartItemsLoading->{
                    Log.e("TAG","loading")
                    binding.progressBar2.isVisible = true
                    binding.totalAmountText.isVisible = false
                    binding.emptycartImg.isVisible = false
                    binding.buyBTN.isVisible = false

                }
                is DashboardViewModel.SetupEvent.ShopItems->{
                    shoppingCartAdapter = ShoppingCartAdapter(requireContext(),events.data)
                    item = events.data
                    setupRecyclerview()
                    var total = 0
                    for(i in events.data){
                        var cost = i.pCost.replace("₹","")
                        var newCost = cost.replace(",","")
                        total+= newCost.toInt() * i.pCount.toInt()
                    }
                   binding.totalAmountText.text = "Total Amount(Inc.GST) : ₹${total.toString()}"
                    binding.progressBar2.isVisible = false
                    binding.totalAmountText.isVisible = true
                    binding.buyBTN.isVisible = true
                }
                is DashboardViewModel.SetupEvent.ShopCartItemsEmpty->{
                    binding.emptycartImg.isVisible = true
                    binding.progressBar2.isVisible = false
                    binding.totalAmountText.isVisible = false

                }
                else->Unit
            }

        }
    }

    private fun observeBuy(){
        lifecycleScope.launch {
            dashboardViewModel.deleteStatus.collect{events->
                when(events){
                    is DashboardViewModel.SetupEvent.ShopCartItemsLoading->{
                        Toast.makeText(requireContext(), "InProgress", Toast.LENGTH_SHORT).show()
                    }
                    is DashboardViewModel.SetupEvent.DeletedConfirmation->{
                        findNavController()?.navigate(R.id.navigation_home)
                        Toast.makeText(requireContext(), "Purchased", Toast.LENGTH_SHORT).show()
                    }
                    else-> Unit
                }

            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}