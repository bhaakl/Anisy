package com.bhaakl.newsapp.presentation.ui.fragments.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private lateinit var top: Animation
    private lateinit var bottom: Animation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().window.navigationBarColor =
            requireContext().getColor(R.color.my_color_700)
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.my_color_700)

        top = AnimationUtils.loadAnimation(requireContext(), R.anim.top)
        bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom)

        binding.spImg.animation = top
        binding.spText.animation = bottom
    }
}