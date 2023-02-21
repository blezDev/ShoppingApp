package com.blez.shoppingapp.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.blez.shoppingapp.R
import com.blez.shoppingapp.databinding.FragmentNotificationsBinding
import com.blez.shoppingapp.ui.login.LoginActivity
import com.blez.shoppingapp.util.TokenManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private lateinit var tokenManager : TokenManager

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        tokenManager= TokenManager(requireContext())

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailText.text = "Email Address : ${tokenManager.getUserName()}@gmail.com"
        binding.nameText.text = "Name: ${tokenManager.getGivenName()}"
        Glide.with(requireContext()).load(tokenManager.getPic()).into(binding.profileImg)
        binding.loginBTN.setOnClickListener {
            tokenManager.deteleCredit()
           findNavController().navigate(R.id.action_navigation_users_to_loginActivity)

        }
        binding.purchasedHistoryText.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_users_to_purchasedHistory)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}