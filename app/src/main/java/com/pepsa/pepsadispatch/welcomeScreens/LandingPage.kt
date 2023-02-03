package com.pepsa.pepsadispatch.welcomeScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.FragmentLandingPageBinding
import com.pepsa.pepsadispatch.shared.utils.AppUtils.changeStatusBarColor

class LandingPage : Fragment() {
    private lateinit var continueBtn: Button
    private lateinit var binding: FragmentLandingPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusBarColor(R.color.landing_page_status_bar)
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_landing_page, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        continueBtn.setOnClickListener {
            findNavController().navigate(R.id.action_landingPage_to_loginFragment)
        }
    }

    private fun initViews() {
        with(binding) {
            continueBtn = button
        }
    }
}
