package com.example.king_bob_nae.features.intro.signin.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.example.king_bob_nae.R
import com.example.king_bob_nae.base.BaseFragment
import com.example.king_bob_nae.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.apply {
            btnBack.setOnClickListener {
                it.findNavController().navigate(R.id.action_signInFragment_to_introFragment)
            }
            tvFindPasswd.setOnClickListener {
                it.findNavController().navigate(R.id.action_signInFragment_to_checkEmailFragment2)
            }
        }
    }
}
