package com.weiran.mynowinandroid.ui.pages.foryou.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.weiran.mynowinandroid.ui.pages.foryou.ForYouViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun ForYouRoute(onNavigateToWeb: (url: String) -> Unit = {}) {
    val viewModel: ForYouViewModel = getViewModel()
    rememberCoroutineScope().launch { viewModel.fetchData() }
    ForYouScreen(viewModel, onNavigateToWeb)
}