package com.weiran.mynowinandroid.ui.pages.interest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.weiran.mynowinandroid.ui.pages.interest.InterestViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun InterestsRoute(viewModel: InterestViewModel = getViewModel()) {
    rememberCoroutineScope().launch { viewModel.fetchData() }
    InterestScreen(viewModel)
}