package com.bhaakl.anisy.presentation.ui.fragments.main

import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.anisy.R
import com.bhaakl.anisy.databinding.FlowFragmentMainBinding
import com.bhaakl.anisy.presentation.base.BaseFlowFragment

class MainFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_main, R.id.nav_host_fragment_main
) {

    private val binding by viewBinding(FlowFragmentMainBinding::bind)

    override fun setupNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}