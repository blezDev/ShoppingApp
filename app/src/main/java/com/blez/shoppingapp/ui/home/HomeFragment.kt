package com.blez.shoppingapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.blez.shoppingapp.R
import com.blez.shoppingapp.adapter.ShoppingItemAdapter
import com.blez.shoppingapp.data.CreateCartItem
import com.blez.shoppingapp.databinding.FragmentHomeBinding
import com.blez.shoppingapp.util.TokenManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel : HomeViewModel by viewModels()
 private lateinit var binding : FragmentHomeBinding
 private lateinit var tokenManager: TokenManager

    private  var shoppingItemAdapter: ShoppingItemAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)
        tokenManager = TokenManager(requireContext())
        binding.welcomeText.text = "HI,${tokenManager.getUserName()}"
        Glide.with(requireContext()).load("https://media.tenor.com/yCFHzEvKa9MAAAAi/hello.gif").into(binding.hiImg)
        setupRecyclerview()
        subscribeToObserver()
        homeViewModel.getItems()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    private fun observeCreateCart() = lifecycleScope.launch {
        homeViewModel.createStatus.collect{events->
            when(events){
                is HomeViewModel.SetupEvent.ShopItemsLoading->{
                    Toast.makeText(requireContext(), "InProgress", Toast.LENGTH_SHORT).show()
                }
                is HomeViewModel.SetupEvent.ShopCartItemCreated->{
                    Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }

        }
    }

    private fun subscribeToObserver() = lifecycleScope.launchWhenCreated {
        homeViewModel.items.collect{events->
            when(events) {
                is HomeViewModel.SetupEvent.ShopItemsLoading->{
                    Log.e("TAG","loading")
                    binding.progressBar.isVisible = true

                }
                is HomeViewModel.SetupEvent.ShopItems->{
                    shoppingItemAdapter = ShoppingItemAdapter(requireContext(),events.data)

                    setupRecyclerview()
                    shoppingItemAdapter?.let {
                        it.onItemClick = {
                            homeViewModel.createCartItem(CreateCartItem(
                                email = tokenManager.getEmail()!!,
                                pName = it.pName,
                                pCompany = it.pCompany,
                                pCost = it.pCost,
                                pCount = it.pCount,
                                pDescription = it.pDescription,
                                pImage = it.pImage,
                                token = tokenManager.getToken()!!

                            ))
                            observeCreateCart()
                        }
                    }
                    shoppingItemAdapter?.let {
                        it.onImgClick = {
                            findNavController()?.navigate(R.id.action_navigation_home_to_detailFragment,Bundle().apply { putString("pName" , it.pName) })
                        }
                    }
                    binding.progressBar.isVisible = false
                }
                else->Unit
            }

        }
    }
    private fun setupRecyclerview(){
        binding.shopItemsRecyclerView.apply {
            adapter = shoppingItemAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
    }
}