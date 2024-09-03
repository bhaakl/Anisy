package com.bhaakl.newsapp.presentation.ui.fragments.main

import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.FlowFragmentMainBinding
import com.bhaakl.newsapp.presentation.base.BaseFlowFragment

class MainFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_main, R.id.nav_host_fragment_main
) {

    private val binding by viewBinding(FlowFragmentMainBinding::bind)

    override fun setupNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}