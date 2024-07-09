package com.example.newsapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.App
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.room.AppDB
import com.example.newsapp.room.repo.UserRepository
import com.example.newsapp.utils.AddCommentAdapter
import com.example.newsapp.viewmodel.MainViewModel
import com.example.newsapp.viewmodel.MainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var commentShowLayout: RecyclerView
    private lateinit var database: AppDB
    private lateinit var adapter: AddCommentAdapter

    private val viewModel: MainViewModel by viewModels {
        val userRepository = UserRepository((applicationContext as App).database.userDao())
        MainViewModelFactory(userRepository)
    }

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

        //отображение Класс/Школа
        binding.btnClass.isSelected = true
        binding.btnClass.setOnClickListener {
            binding.btnClass.isSelected = true
            binding.btnSchool.isSelected = false
            binding.classTenkai.visibility = View.VISIBLE
            binding.scTenkai.visibility = View.GONE
        }
        binding.btnSchool.setOnClickListener {
            binding.btnSchool.isSelected = true
            binding.btnClass.isSelected = false
            binding.classTenkai.visibility = View.GONE
            binding.scTenkai.visibility = View.VISIBLE
        }

        //Like class
        val likeBtn = binding.likeBtn
        likeBtn.setOnCheckedChangeListener { checkBox, isChecked ->
            viewModel.toggleLike(auth.currentUser?.email.toString())
        }
        val likeBtn1 = binding.likeBtn1
        likeBtn1.setOnCheckedChangeListener { checkBox, isChecked ->
            viewModel.toggleLike(auth.currentUser?.email.toString())
        }

        // Like sc
        val likeBtnSc = binding.likeBtnSc
        likeBtnSc.setOnCheckedChangeListener { checkBox, isChecked ->
            viewModel.toggleLike(auth.currentUser?.email.toString())
        }
        val likeBtnSc1 = binding.likeBtnSc1
        likeBtnSc1.setOnCheckedChangeListener { checkBox, isChecked ->
            viewModel.toggleLike(auth.currentUser?.email.toString())
        }

        //comment
        database = (applicationContext as App).database
        commentShowLayout = findViewById(R.id.commentShowLayout)

        binding.showComment.setOnClickListener {
            binding.addCommentInputLayout.visibility =
                if (binding.addCommentInputLayout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        this.adapter = AddCommentAdapter(database.commentQuery(), lifecycleScope)
        commentShowLayout.adapter = this.adapter
        commentShowLayout.layoutManager = LinearLayoutManager(this)

        val textInputLayout = binding.addCommentInputText
        textInputLayout.setEndIconOnClickListener {
            val commentText = binding.addedCommentEditText.text.toString()
            var commentAuthor = ""
            lifecycleScope.launch {
                viewModel.getUserByEmail(auth.currentUser?.email.toString()).collect { user ->
                    if (user != null && commentText.isNotEmpty()) {
                        commentAuthor = user.name
                        this@MainActivity.adapter.addComment(commentText, commentAuthor)
                    }
                }
            }
        }
    }
}