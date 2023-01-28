package com.weiran.mynowinandroid.modules.foryou.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.weiran.mynowinandroid.modules.foryou.ForYouViewModel

@Composable
fun ForYouRoute(viewModel: ForYouViewModel = hiltViewModel()) {
    ForYouScreen(viewModel)
}