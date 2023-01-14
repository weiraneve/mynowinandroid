package com.weiran.mynowinandroid.foryou.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.weiran.mynowinandroid.foryou.ForYouViewModel

@Composable
fun ForYouRoute(viewModel: ForYouViewModel = hiltViewModel()) {
    ForYouScreen(viewModel)
}