package com.bhaakl.newsapp.presentation.ui.fragments.main.home

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.FragmentHomeBinding
import com.bhaakl.newsapp.databinding.ListAnimeLayoutBinding
import com.bhaakl.newsapp.presentation.base.BaseFragment
import com.bhaakl.newsapp.presentation.extensions.activityNavController
import com.bhaakl.newsapp.presentation.extensions.navigateSafely
import com.bhaakl.newsapp.presentation.model.AnimeUi
import com.bhaakl.newsapp.presentation.ui.fragments.main.MainFlowFragmentDirections
import com.bhaakl.newsapp.presentation.ui.fragments.main.home.adapter.AnimePagingAdapter
import com.bhaakl.newsapp.presentation.ui.fragments.main.home.adapter.paging.CommonLoadStateAdapter
import com.bhaakl.newsapp.domain.util.SingleEvent
import com.bhaakl.newsapp.domain.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()
    override val binding by viewBinding(FragmentHomeBinding::bind)

    private lateinit var listbinding: ListAnimeLayoutBinding
    private lateinit var animeAdapter: AnimePagingAdapter

    override fun initialize() {
        val includedLayoutView = binding.root.findViewById<View>(R.id.crl_anime_list)
        listbinding = ListAnimeLayoutBinding.bind(includedLayoutView)
        animeAdapter = AnimePagingAdapter(viewModel::openNewsDetails)
        setupAnimeRecycler()

        //Like class
        /*val likeBtn = binding.likeBtn
        likeBtn.setOnCheckedChangeListener { checkBox, isChecked ->
            newsViewModel.toggleLike(auth.currentUser?.email.toString())
        }
        val likeBtn1 = binding.likeBtn1
        likeBtn1.setOnCheckedChangeListener { checkBox, isChecked ->
            newsViewModel.toggleLike(auth.currentUser?.email.toString())
        }*/
    }

    // start region lifecycle
    override fun onResume() {
        super.onResume()
        listbinding.swrefreshAnimeRv.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        listbinding.swrefreshAnimeRv.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listbinding.rvNewsList.adapter = null
    }
    // end region lifecycle

    private fun setupAnimeRecycler() = with(listbinding) {
        val loadStateAdapter = CommonLoadStateAdapter(
            { animeAdapter.retry() },
            { m -> viewModel.showSnackBarMessage(m) })
            .apply {
                collectFlowSafely(lifecycleState = Lifecycle.State.STARTED) {
                    animeAdapter.loadStateFlow.collectLatest { loadStates ->
                        loadState = loadStates.refresh
                    }
                }
            }
        rvNewsList.layoutManager = LinearLayoutManager(context)
        rvNewsList.setHasFixedSize(true)
        rvNewsList.adapter = animeAdapter
            .withLoadStateFooter(
                footer = loadStateAdapter
            )

        animeAdapter.addLoadStateListener { loadStates ->
            swrefreshAnimeRv.isRefreshing = loadStates.refresh is LoadState.Loading
            rvNewsList.isVisible =
                loadStates.refresh is LoadState.NotLoading || loadStates.refresh is LoadState.Error
            swrefreshAnimeRv.setOnRefreshListener {
                swrefreshAnimeRv.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }
    }

    override fun setupRequests() {
        fetchAnime()
    }

    private fun fetchAnime() {
        viewModel.animeFlowDataPaging.collectPaging {
            animeAdapter.submitData(it)
        }
    }

    override fun setupSubscribers() {
//        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
//        observe(recipesListViewModel.noSearchFound, ::noSearchResult)

        viewModel.openPostDetails.observeSafely(
            lifecycleOwner = viewLifecycleOwner,
            lifecycleState = Lifecycle.State.STARTED
        ) { navigateToDetailsScreen(it) }
//        observeToast(viewModel.showToast)
        observeSnackBarMessages(viewModel.showSnackBar)
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<AnimeUi>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val action = MainFlowFragmentDirections.actionMainFlowFragmentToDetailsFragment(it)
            activityNavController().navigateSafely(action)
        }
    }
    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(
            viewLifecycleOwner,
            { animeAdapter.retry() },
            event,
            Snackbar.LENGTH_LONG
        )
    }
    /*private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }*/


    override fun setupListeners() {
        hideHomeTitleWhenCollapsing()
        scrollToTop()
    }

    private fun hideHomeTitleWhenCollapsing() = with(listbinding) {
        topAppBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange

            val collapseFactor = abs(verticalOffset / totalScrollRange.toFloat())

            if (collapseFactor > 0.5f) {
                if (tvHomeTitle.visibility == View.VISIBLE) {
                    tvHomeTitle.animate().alpha(0f).setDuration(200).withEndAction {
                        tvHomeTitle.visibility = View.GONE
                    }
                }
            } else {
                if (tvHomeTitle.visibility == View.GONE) {
                    tvHomeTitle.visibility = View.VISIBLE
                    tvHomeTitle.alpha = 0f
                    tvHomeTitle.animate().alpha(1f).setDuration(200).start()
                }
            }
        }
    }
    private fun scrollToTop() =
        listbinding.flBtnHome.setOnClickListener {
            listbinding.rvNewsList.smoothScrollToPosition(0)
            listbinding.topAppBar.setExpanded(true, true)
        }


    // util-funs

}