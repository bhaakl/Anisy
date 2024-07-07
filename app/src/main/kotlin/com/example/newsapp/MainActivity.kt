package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newsapp.models.NewsApiResponse
import com.example.newsapp.models.NewsHeadlines
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), SelectListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CustomAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = getColor(this, R.color.black)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            startActivity(Intent(applicationContext, login::class.java))
            finish()
        }

        binding.shimmer.startShimmer()

        // Здесь ты можешь загрузить новости из нужной тебе категории или источника
        // Например:
        // val manager = RequestManager(this)
        // manager.getNewsHeadlines(listener, "technology", null)

        // bottom nav
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.selectedItemId = R.id.navigation_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_eventpick -> {
                    startActivity(Intent(this@MainActivity, PickEventActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }

    }

    private val listener: OnFetchDataListener<NewsApiResponse?> =
        object : OnFetchDataListener<NewsApiResponse?> {
            override fun onFetchData(list: List<NewsHeadlines>, message: String?) {
                if (list.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Данные не найдены!", Toast.LENGTH_SHORT).show()
                } else {
                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                    binding.recyclerMain.visibility = View.VISIBLE
                    showNews(list)
                }
            }

            override fun onError(message: String?) {
                Toast.makeText(
                    this@MainActivity,
                    "Проблема с подключением к интернету!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private fun showNews(list: List<NewsHeadlines>) {
        binding.recyclerMain.setHasFixedSize(true)
        binding.recyclerMain.layoutManager = GridLayoutManager(this, 1)
        adapter = CustomAdapter(this, list, this)
        binding.recyclerMain.adapter = adapter
    }

    override fun onNewsClicked(headlines: NewsHeadlines?) {
        if (headlines != null) {
            startActivity(
                Intent(this@MainActivity, DetailsView::class.java)
                    .putExtra("data", headlines)
            )
        } else {
            Toast.makeText(
                this@MainActivity,
                "Что-то пошло не так!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about_us -> startActivity(Intent(this@MainActivity, AboutUs::class.java))
            R.id.sign_out -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, login::class.java))
                finish()
            }

            R.id.summarizer -> startActivity(Intent(this@MainActivity, Summarizer::class.java))
            R.id.favourite -> startActivity(Intent(this@MainActivity, Favourite::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}