package com.pepsa.pepsadispatch.welcomeScreens.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.pepsa.pepsadispatch.R
import com.pepsa.pepsadispatch.databinding.FragmentLoginBinding
import com.pepsa.pepsadispatch.mian.presentation.ui.activities.MainApp

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onResume() {
        super.onResume()
        loginButton.setOnClickListener {
            val intent = Intent(requireActivity(), MainApp::class.java)
            requireContext().startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun initViews() {
        with(binding) {
            loginButton = loginBtn
        }
    }
}
