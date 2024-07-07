package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.newsapp.models.NewsHeadlines
import com.example.newsapp.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.newsapp.room.AppDB
import com.example.newsapp.room.FavouriteModel
import kotlinx.coroutines.*
import java.util.*

class DetailsView : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var headlines: NewsHeadlines
    private var dialog: AlertDialog? = null
    private var res: String? = null
    private var imgUrl: String? = null
    private var sentiment: String? = null

    companion object {
        const val DB_NAME = "favDb"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        headlines = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data", NewsHeadlines::class.java)
        } else {
            intent.getParcelableExtra("data")
        } ?: return

        binding.textDetailTitle.text = headlines.title
        binding.textDetailAuthor.text = headlines.author
        binding.textDetailTime.text = headlines.publishedAt
        imgUrl = headlines.urlToImage

        generateSummary()

        binding.readMore.setOnClickListener {
            val intent = Intent(this, webView::class.java)
            intent.putExtra("url", headlines.url)
            startActivity(intent)
        }
    }

    private fun generateSummary() {val builder = AlertDialog.Builder(this)
        val progressBar = ProgressBar(this)
        builder.setView(progressBar)
            .setTitle("Генерируется краткое содержание, пожалуйста, подождите...")
            .setCancelable(false)
        dialog = builder.create()
        dialog?.show()

        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                if (!Python.isStarted()) {
                    Python.start(AndroidPlatform(this@DetailsView))
                }
                val py = Python.getInstance()
                val pyobj = py.getModule("myfile")
                val obj = pyobj.callAttr("main", headlines.url)
                obj.toString()
            }

            val arr = result.split("999").toTypedArray() // Преобразуем в массив
            res = arr.getOrNull(0)
            sentiment = arr.getOrNull(1)
            if (res.isNullOrEmpty()) {
                res = headlines.description ?: "Извините! Не удалось сгенерировать краткое содержание"
            }

            dialog?.dismiss()

            // Используем View Binding для доступа к элементам UI
            Glide.with(this@DetailsView).load(imgUrl).into(binding.imgDetailNews)
            binding.textDetailContent.text = res

            if (checkFav(headlines.title, this@DetailsView)) {
                binding.addToFav.setImageResource(R.drawable.hear)
            } else {
                binding.addToFav.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            binding.addToFav.setOnClickListener {lifecycleScope.launch { // Запускаем корутину внутри setOnClickListener
                if (checkFav(headlines.title, this@DetailsView)) {
                    removeFav(headlines.title, res, this@DetailsView, binding.addToFav)
                } else {
                    addFav(headlines.title, res)
                }
            }
            }

            binding.sentimentTxt.text = sentiment?.uppercase(Locale.ROOT)
            when (sentiment?.uppercase(Locale.ROOT)) {
                "POSITIVE" -> binding.sentimentIcon.setImageResource(R.drawable.ic_baseline_circle_24_green)
                "NEGATIVE" -> binding.sentimentIcon.setImageResource(R.drawable.ic_baseline_circle_24_red)
                else -> binding.sentimentIcon.setImageResource(R.drawable.ic_baseline_circle_24_yellow)
            }
        }
    }

    private fun addFav(title: String?, content: String?) {
        binding.addToFav.setImageResource(R.drawable.hear)
        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(this@DetailsView, AppDB::class.java, DB_NAME).build()
            try {
                db.favouriteQuery().insertData(FavouriteModel(title, content))
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailsView, "Добавлено в избранное", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailsView, "Не удалось добавить", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun removeFav(title: String?, content: String?, context: Context, imageView: ImageView) {
        AlertDialog.Builder(context)
            .setTitle("Удалить..?")
            .setMessage("Вы хотите удалить эту статью из избранного?")
            .setPositiveButton("Удалить") { dialog, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    val db = Room.databaseBuilder(context, AppDB::class.java, DB_NAME).build()
                    val userDao = db.favouriteQuery()
                    try {
                        val favouriteToDelete = title?.let { userDao.getFavouriteByTitle(it) }
                        favouriteToDelete?.let {
                            userDao.delete(it)
                        }
//                        userDao?.delete(content)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                            imageView.setImageResource(R.drawable.ic_baseline_favorite_24)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Не удалось удалить", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private suspend fun checkFav(title: String?, context: Context): Boolean {
        val db = Room.databaseBuilder(context, AppDB::class.java, DB_NAME).allowMainThreadQueries().build()
        val userDao = db.favouriteQuery()
        return userDao.isExists(title.toString())
    }
}