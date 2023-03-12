package com.weiran.mynowinandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.weiran.mynowinandroid.common.utils.NetworkMonitor
import com.weiran.mynowinandroid.ui.pages.home.ui.Home
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val networkMonitor: NetworkMonitor by inject()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Home(
                networkMonitor = networkMonitor,
                windowSizeClass = calculateWindowSizeClass(this)
            )
        }
    }
}