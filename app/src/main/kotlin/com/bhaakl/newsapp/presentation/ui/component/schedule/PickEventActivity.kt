package com.bhaakl.newsapp.presentation.ui.component.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.databinding.ActivityEventBinding
import com.bhaakl.newsapp.data.AppDB
import com.bhaakl.newsapp.ui.component.login.login
import com.bhaakl.newsapp.ui.component.newsland.NewsActivity
import com.bhaakl.newsapp.ui.component.profile.ProfileActivity
import com.bhaakl.newsapp.ui.component.schedule.adapter.AddEventAdapter
import com.bhaakl.newsapp.ui.component.schedule.adapter.CalendarAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class PickEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var monthYearText: TextView
    private lateinit var calendarRecyclerView: RecyclerView
    private var selectedDate: LocalDate = LocalDate.now()

    private lateinit var eventShowLayout: RecyclerView
    @Inject lateinit var database: AppDB
    private lateinit var adapter: AddEventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if (user == null) {
            startActivity(Intent(applicationContext, login::class.java))
            finish()
        }

        // Bottom Navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.navigation_eventpick
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    true
                }

                R.id.navigation_eventpick -> {
                    true
                }

                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }

        // Calendar
        monthYearText = findViewById(R.id.monthYearTV)
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        setMonthView()
        binding.todayDate.text = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        eventShowLayout = findViewById(R.id.eventShowLayout)

        // add event
        adapter = AddEventAdapter(database.calEventTextQuery(), lifecycleScope)
        eventShowLayout.adapter = adapter
        eventShowLayout.layoutManager = LinearLayoutManager(this)

        binding.addEventButton.setOnClickListener {
//            adapter.deleteAllEvents()
            addEventAction()
        }
    }

    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth) { position, dayText ->
            onItemClick(position, dayText)
        }
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): List<String> {
        val daysInMonthArray = mutableListOf<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        // Расчет дней предыдущего месяца
        val prevMonth = yearMonth.minusMonths(1)
        val daysInPrevMonth = prevMonth.lengthOfMonth()
        var prevMonthDay = daysInPrevMonth - dayOfWeek + 1

        // Расчет текущего дня
        val todayDay =
            if (date.monthValue == LocalDate.now().monthValue && date.year == LocalDate.now().year) date.dayOfMonth else -1

        for (i in 1..42) {
            if (i <= dayOfWeek) {
                daysInMonthArray.add(prevMonthDay++.toString() + 'f') // Дни предыдущего месяца
            } else if (i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add((i - (daysInMonth + dayOfWeek)).toString() + 'f') // Дни следующего месяца
            } else {
                if (i - dayOfWeek == todayDay) {
                    daysInMonthArray.add((i - dayOfWeek).toString() + 't') // Сегодняшний день
                } else {
                    daysInMonthArray.add((i - dayOfWeek).toString()) // Дни текущего месяца
                }
            }
        }
        return daysInMonthArray
    }

    private fun previousMonthDays(date: LocalDate): Int {
        val prevMonth = date.minusMonths(1)
        return YearMonth.from(prevMonth).lengthOfMonth()
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("ru"))
        val monthYear = date.format(formatter)
        return monthYear.substring(0, 1).uppercase() + monthYear.substring(1)
    }

    fun previousMonthAction(view: View) {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View) {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    private fun addEventAction() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_form_add_event, null)
        val editTextName = dialogView.findViewById<TextInputEditText>(R.id.addedEventEditText)

        MaterialAlertDialogBuilder(this)
            .setTitle("Добавление события")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { dialog, _ ->
                val name = editTextName.text.toString()
                if (name.isBlank()) {
                    dialog.dismiss()
                    return@setPositiveButton
                }
                adapter.addEvent(name)
                dialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun onItemClick(position: Int, dayText: String) {
        if (dayText.isNotEmpty()) {
            val message = "Selected Date: $dayText ${monthYearFromDate(selectedDate)}"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}