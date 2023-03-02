package com.weiran.mynowinandroid.modules.foryou.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.weiran.mynowinandroid.modules.foryou.ForYouViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun ForYouRoute(viewModel: ForYouViewModel = getViewModel()) {
    rememberCoroutineScope().launch { viewModel.fetchData() }
    ForYouScreen(viewModel)
}