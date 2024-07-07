package com.example.newsapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.newsapp.databinding.ActivitySummarizerBinding
import kotlinx.coroutines.*

class Summarizer : AppCompatActivity() {

    private lateinit var binding: ActivitySummarizerBinding
    private var url: String? = null
    private var res: String? = null
    private var sentiment: String? = null
    private var dialog: AlertDialog? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummarizerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.submit.setOnClickListener {
            url = binding.pasteLink.text.toString()
            Log.i("hello", url!!)
            if (url!!.isNotBlank()) {
                generateSummary()
            } else {
                Toast.makeText(this, "Пожалуйста, вставьте ссылку", Toast.LENGTH_SHORT).show()
            }
        }

        binding.reset.setOnClickListener { binding.pasteLink.text.clear() }
    }

    private fun generateSummary() = coroutineScope.launch {
        val builder = AlertDialog.Builder(this@Summarizer)
        builder.setView(R.layout.progress_dialog)
        dialog = builder.create()
        dialog?.show()

        val result = withContext(Dispatchers.IO) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this@Summarizer))
            }
            val py =Python.getInstance()
            val pyobj = py.getModule("myfile")
            val obj = pyobj.callAttr("main", url)
            obj.toString()
        }

        val arr = result.split("999".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        res = arr[0].ifEmpty { "Извините! Не удалось создать краткое содержание" }
        sentiment = arr[1]

        dialog?.dismiss()
        binding.content.text = "$res\n"
        binding.sentimentTxt.text = sentiment!!.uppercase()
        binding.sentimentIcon.setImageResource(
            when (sentiment!!.uppercase()) {
                "POSITIVE" -> R.drawable.ic_baseline_circle_24_green
                "NEGATIVE" -> R.drawable.ic_baseline_circle_24_red
                else -> R.drawable.ic_baseline_circle_24_yellow
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        dialog?.dismiss() // Закрываем диалог при уничтожении Activity
    }
}