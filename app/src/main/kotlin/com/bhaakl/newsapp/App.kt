package com.bhaakl.newsapp

import android.app.Application
import com.bhaakl.newsapp.data.AppDB
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {}