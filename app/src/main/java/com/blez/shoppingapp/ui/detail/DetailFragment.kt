package com.blez.shoppingapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.blez.shoppingapp.R
import com.blez.shoppingapp.data.CreateCartItem
import com.blez.shoppingapp.data.ShopItem
import com.blez.shoppingapp.databinding.FragmentDetailBinding
import com.blez.shoppingapp.ui.home.HomeViewModel
import com.blez.shoppingapp.util.TokenManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
private lateinit var binding : FragmentDetailBinding
private val detailViewModel : DetailViewModel by viewModels()
  private lateinit var navArgs : String
  private lateinit var item : CreateCartItem
  private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        navArgs = arguments?.getString("pName","").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel.getSelectedItem(navArgs)
        tokenManager = TokenManager(requireContext())
        observeToUI()
        binding.AddCartBTN.setOnClickListener {
           detailViewModel.createCartItem(item)
            observeCreateCart()
        }


    }

    private fun observeToUI(){
        lifecycleScope.launchWhenCreated {
            detailViewModel.createStatus.collect{events->
                when(events){
                    is DetailViewModel.SetupEvent.ShopItemsLoading->{
                        binding.progressBar3.isVisible = true
                    }
                    is DetailViewModel.SetupEvent.ShopItem->{
                        binding.progressBar3.isVisible = false
                        allocateData(events.data)
                        val it = events.data
                        item = CreateCartItem(
                            email = tokenManager.getEmail()!!,
                            pName = it.pName,
                            pCompany = it.pCompany,
                            pCost = it.pCost,
                            pCount = it.pCount,
                            pDescription = it.pDescription,
                            pImage = it.pImage,
                            token = tokenManager.getToken()!!
                        )

                    }
                    else ->Unit
                }


            }
        }
    }

    private fun allocateData(data: ShopItem) {
        binding.pNameText.text = data.pName
        Glide.with(requireContext()).load(data.pImage).into(binding.pImage)
        binding.pCompanyName.text = data.pCompany
        binding.pCost.text = "Price : ${data.pCost}"
        binding.pDescription.text = data.pDescription
    }



    private fun observeCreateCart() = lifecycleScope.launch {
        detailViewModel.createStatusItem.collect{events->
            when(events){
                is DetailViewModel.SetupEvent.ShopItemsLoading->{
                    Toast.makeText(requireContext(), "InProgress", Toast.LENGTH_SHORT).show()
                }
                is DetailViewModel.SetupEvent.ShopCartItemCreated->{
                    Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }

        }
    }

}