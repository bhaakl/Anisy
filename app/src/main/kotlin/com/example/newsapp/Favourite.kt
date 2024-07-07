package com.example.newsapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.newsapp.models.FavouriteData
import com.example.newsapp.databinding.ActivityFavouriteBinding
import com.example.newsapp.room.AppDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Favourite : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding
    private var favAdapter: FavAdapter? = null
    private var favlist: ArrayList<FavouriteData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFav()}

    private fun loadFav() {
        initList()
        binding.recyclerFav.setHasFixedSize(true)
        binding.recyclerFav.layoutManager = LinearLayoutManager(this)

        favAdapter = FavAdapter(this, favlist)
        binding.recyclerFav.adapter = favAdapter

        favAdapter?.notifyDataSetChanged()
    }

    private fun initList() {
        val db = Room.databaseBuilder(
            this@Favourite,
            AppDB::class.java,
            dbName
        ).allowMainThreadQueries().build()
        val userDao = db.favouriteQuery()

        val list = userDao.getAllFavourites()
        list.forEach { data ->
            favlist.add(FavouriteData(data.title, data.content))
        }
    }

    fun removeFav(title: String?, content: String?, context: Context?) {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Удалить") { dialog, which ->
            lifecycleScope.launch(Dispatchers.IO) {
                val db = Room.databaseBuilder(context!!, AppDB::class.java, dbName).build()
                val userDao = db.favouriteQuery()
                try {
                    val favouriteToDelete = title?.let { userDao.getFavouriteByTitle(it) }
                    favouriteToDelete?.let {
                        userDao.delete(it)
                    }
                    withContext(Dispatchers.Main) { // Возвращаемся в главный поток для UI
                        Toast.makeText(context, "Удалено..", Toast.LENGTH_SHORT).show()
                        loadFav() // Обновляем список после удаления
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Не удалось удалить..", Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    db.close() // Закрываем соединение с базой данных
                }
            }
        }
            .setNegativeButton("Отмена") { dialog, which -> dialog.dismiss() }.show()
    }

    override fun onResume() {
        super.onResume()
        try {
            loadFav()
        } catch (e: Exception) {
            Log.i("check", "onResume: $e")
        }
    }

    companion object {
        var dbName: String = "favDb"
    }
}