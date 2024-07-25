package com.bhaakl.newsapp.presentation.ui.component.newsland

import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhaakl.newsapp.NEWS_ITEM_KEY
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.ActivityMainBinding
import com.bhaakl.newsapp.data.AppDB
import com.bhaakl.newsapp.data.Resource
import com.bhaakl.newsapp.data.dto.news.Anime
import com.bhaakl.newsapp.data.dto.news.AnimeItem
import com.bhaakl.newsapp.ui.component.schedule.PickEventActivity
import com.bhaakl.newsapp.ui.component.profile.ProfileActivity
import com.bhaakl.newsapp.ui.base.BaseActivity
import com.bhaakl.newsapp.ui.component.details.DetailsActivity
import com.bhaakl.newsapp.ui.component.login.login
import com.bhaakl.newsapp.ui.component.newsland.adapter.NewsAdapter
import com.bhaakl.newsapp.utils.SingleEvent
import com.bhaakl.newsapp.utils.observe
import com.bhaakl.newsapp.utils.observeEvent
import com.bhaakl.newsapp.utils.setupSnackbar
import com.bhaakl.newsapp.utils.showToast
import com.bhaakl.newsapp.utils.toGone
import com.bhaakl.newsapp.utils.toVisible
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(applicationContext, login::class.java))
            finish()
        }*/

        binding.shimmer.startShimmer()

        setupNavigation()

        /*
        // bottom nav
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.selectedItemId = R.id.navigation_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }

                R.id.navigation_eventpick -> {
                    startActivity(Intent(this, PickEventActivity::class.java))
                    true
                }

                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }

        // news list
        binding.rvNewsList.layoutManager = LinearLayoutManager(this)
        binding.rvNewsList.setHasFixedSize(true)
        newsViewModel.getRecipes()
        */
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

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        when {
             user != null  -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
            else -> {
                navGraph.setStartDestination(R.id.signFlowFragment)
            }
        }
        navController.graph = navGraph
    }

    private fun bindListData(anime: Anime) {
        if (!(anime.data.isNullOrEmpty())) {
            newsAdapter = NewsAdapter(newsViewModel, anime.data)
            binding.rvNewsList.adapter = newsAdapter
            binding.shimmer.stopShimmer()
//            binding.shimmerParent.visibility = View.GONE
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<AnimeItem>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = Intent(this, DetailsActivity::class.java).apply {
                putExtra(NEWS_ITEM_KEY, it)
            }
            startActivity(nextScreenIntent)
        }
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    private fun showDataView(show: Boolean) {
        binding.tvNoData.visibility = if (show) View.GONE else View.VISIBLE
        binding.rvNewsList.visibility = if (show) View.VISIBLE else View.GONE
        binding.pbLoading.toGone()
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.rvNewsList.toGone()
    }

    private fun handleNewsList(status: Resource<Anime>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(anime = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { newsViewModel.showToastMessage(it) }
            }
        }
    }

    override fun observeViewModel() {
        observe(newsViewModel.animeLiveData, ::handleNewsList)
//        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
//        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observeEvent(newsViewModel.openNewsDetails, ::navigateToDetailsScreen)
        observeSnackBarMessages(newsViewModel.showSnackBar)
        observeToast(newsViewModel.showToast)
    }
}