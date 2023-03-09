package com.weiran.mynowinandroid.pages.foryou.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.weiran.mynowinandroid.pages.foryou.ForYouViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun ForYouRoute(navController: NavController) {
    val viewModel: ForYouViewModel = getViewModel()
    rememberCoroutineScope().launch { viewModel.fetchData() }
    ForYouScreen(viewModel, navController)
}