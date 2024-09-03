package com.bhaakl.newsapp.presentation.ui.fragments.main.schedule

import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bhaakl.newsapp.R
import com.bhaakl.newsapp.data.AppDB
import com.bhaakl.newsapp.databinding.FragmentScheduleBinding
import com.bhaakl.newsapp.presentation.base.BaseFragment
import com.bhaakl.newsapp.presentation.ui.fragments.main.home.adapter.paging.CommonLoadStateAdapter
import com.bhaakl.newsapp.presentation.ui.fragments.main.schedule.adapter.AnimeScheduleAdapter
import com.bhaakl.newsapp.presentation.ui.fragments.main.schedule.adapter.CalendarAdapter
import com.bhaakl.newsapp.presentation.ui.fragments.util.ExpandableHeaderItem
import com.bhaakl.newsapp.presentation.ui.fragments.util.groupie.ColumnGroup
import com.bhaakl.newsapp.presentation.ui.fragments.util.groupie.item.CardItem
import com.bhaakl.newsapp.presentation.ui.fragments.util.groupie.item.ColumnItem
import com.bhaakl.newsapp.presentation.ui.fragments.util.groupie.item.HeaderItem
import com.bhaakl.newsapp.domain.util.SingleEvent
import com.bhaakl.newsapp.domain.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<ScheduleViewModel, FragmentScheduleBinding>(R.layout.fragment_schedule) {

    override val viewModel: ScheduleViewModel by viewModels()
    override val binding by viewBinding(FragmentScheduleBinding::bind)
    private var selectedDate: LocalDate = LocalDate.now()

    private val gray: Int by lazy { getColor(requireContext(), R.color.my_gray_color) }
    private val betweenPadding: Int by lazy { resources.getDimensionPixelSize(R.dimen.space_8x) }
    private val rainbow200: IntArray by lazy { resources.getIntArray(R.array.rainbow_200) }

    private lateinit var groupAdapter: GroupieAdapter
    private val scheduleAdapter = AnimeScheduleAdapter()

    @Inject
    lateinit var database: AppDB

    // start region lifecycle
    override fun onResume() {
        super.onResume()
        binding.swrefreshSchedule.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        binding.swrefreshSchedule.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding.rvScheduleList.adapter = null
    }
    // end region lifecycle

    override fun initialize() {
        groupAdapter = GroupieAdapter().apply {
            setOnItemClickListener(onItemClickListener)
            spanCount = 12
        }
        // Calendar
        setMonthView()
        setupScheduleAnimeRecycler()
    }

    override fun setupListeners() = with(binding) {
        prevCalMonth.setOnClickListener {
            previousMonthAction()
        }
        nextCalMonth.setOnClickListener {
            nextMonthAction()
        }
    }

    override fun setupRequests() {
        fetchScheduleAnime()
    }

    private fun fetchScheduleAnime() {
        viewModel.animeScheduleFlowDataPaging.collectPaging {
            scheduleAdapter.submitData(it)
        }
    }

    override fun setupSubscribers() {
        observeSnackBarMessages(viewModel.showSnackBar)
    }

    private fun setupScheduleAnimeRecycler() = with(binding){
        val loadStateAdapter = CommonLoadStateAdapter(
            { scheduleAdapter.retry() },
            { m -> viewModel.showSnackBarMessage(m) })
            .apply {
                collectFlowSafely(lifecycleState = Lifecycle.State.STARTED) {
                    scheduleAdapter.loadStateFlow.collectLatest { loadStates ->
                        loadState = loadStates.refresh
                    }
                }
            }

        val layoutManager = GridLayoutManager(context, groupAdapter.spanCount).apply {
            spanSizeLookup = groupAdapter.spanSizeLookup
        }
        rvScheduleList.apply {
//            addItemDecoration(HeaderItemDecoration(gray, betweenPadding))
            this.layoutManager = layoutManager
            setHasFixedSize(true)
            adapter = groupAdapter
        }
//        groupAdapter.add(AnimeSchedulePagingGroup(scheduleAdapter, loadStateAdapter))

        val subtitleGroup = getString(R.string.exp_group_subtitle)
        val mon = ExpandableHeaderItem("Monday", subtitleGroup)
        groupAdapter.add(ExpandableGroup(mon).apply {
            for (i in 0..1) {
                add(CardItem(getColor(requireContext(), R.color.my_color_orange)))
            }
        })

        val tue = ExpandableHeaderItem("Tuesday", subtitleGroup)
        groupAdapter.add(ExpandableGroup(tue).apply {
            for (i in 0..1) {
                add(CardItem(getColor(requireContext(), R.color.my_color_orange)))
            }
        })

        val wed = ExpandableHeaderItem("Wednesday", subtitleGroup)
        groupAdapter.add(ExpandableGroup(wed).apply {
            for (i in 0..1) {
                add(CardItem(getColor(requireContext(), R.color.my_color_orange)))
            }
        })

        val thur = ExpandableHeaderItem("Thursday", subtitleGroup)
        groupAdapter.add(ExpandableGroup(thur).apply {
            for (i in 0..1) {
                add(CardItem(getColor(requireContext(), R.color.my_color_orange)))
            }
        })

        val fr = ExpandableHeaderItem("Friday", subtitleGroup)
        groupAdapter.add(ExpandableGroup(fr).apply {
            for (i in 0..1) {
                add(CardItem(getColor(requireContext(), R.color.my_color_orange)))
            }
        })

        val sun = ExpandableHeaderItem("Sunday", subtitleGroup)
        groupAdapter.add(ExpandableGroup(sun).apply {
            for (i in 0..1) {
                add(CardItem(getColor(requireContext(), R.color.my_color_orange)))
            }
        })

        // Columns
        groupAdapter.add(Section(HeaderItem("Other")).apply {
            add(makeColumnGroup())
        })

        /*scheduleAdapter.addLoadStateListener { loadStates ->
            swrefreshSchedule.isRefreshing = loadStates.refresh is LoadState.Loading
            rvScheduleList.isVisible =
                loadStates.refresh is LoadState.NotLoading || loadStates.refresh is LoadState.Error
            swrefreshSchedule.setOnRefreshListener {
                swrefreshSchedule.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }*/
    }

    // Util funs
    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(
            viewLifecycleOwner,
            { scheduleAdapter.retry() },
            event,
            Snackbar.LENGTH_LONG
        )
    }

    //calendar
    private fun setMonthView() = with(binding){
        monthYearTV.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, requireContext()) { dayText ->
            onItemClick(dayText)
        }
        calendarRecyclerView.layoutManager = GridLayoutManager(context, 7)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): List<String> {
        val daysInMonthArray = mutableListOf<String>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        // Calculation of days of previous month
        val prevMonth = yearMonth.minusMonths(1)
        val daysInPrevMonth = prevMonth.lengthOfMonth()
        var prevMonthDay = daysInPrevMonth - dayOfWeek + 1

        // Current day calculation
        val todayDay =
            if (date.monthValue == LocalDate.now().monthValue && date.year == LocalDate.now().year) date.dayOfMonth else -1

        for (i in 1..42) {
            if (i <= dayOfWeek) {
                daysInMonthArray.add(prevMonthDay++.toString() + 'f') // previous days month
            } else if (i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add((i - (daysInMonth + dayOfWeek)).toString() + 'f') // next day month
            } else {
                if (i - dayOfWeek == todayDay) {
                    daysInMonthArray.add((i - dayOfWeek).toString() + 't') // today
                } else {
                    daysInMonthArray.add((i - dayOfWeek).toString()) // days current month
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
        val dateTime = date.atTime(LocalTime.now())
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy, HH:mm:ss", Locale("en"))
        val formattedDateTime = dateTime.format(formatter)

        return formattedDateTime.substring(0, 1).uppercase() + formattedDateTime.substring(1)
    }

    private fun previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1)
        setMonthView()
    }

    private fun nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1)
        setMonthView()
    }

    private fun onItemClick(dayText: String) {
        if (dayText.isNotEmpty()) {
            val message = "Selected Date: $dayText ${monthYearFromDate(selectedDate)}"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    // schedule-recyclerview
    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is CardItem && item.text.isNotEmpty()) {
            Toast.makeText(requireContext(), item.text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeColumnGroup(): ColumnGroup {
        val columnItems = ArrayList<ColumnItem>()
        for (i in 1..5) {
            // First five items are red -- they'll end up in a vertical column
            columnItems.add(ColumnItem(rainbow200[0], i))
        }
        for (i in 6..10) {
            // Next five items are pink
            columnItems.add(ColumnItem(rainbow200[1], i))
        }
        return ColumnGroup(columnItems)
    }
}