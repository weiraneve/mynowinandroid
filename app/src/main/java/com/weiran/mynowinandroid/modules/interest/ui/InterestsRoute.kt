package com.weiran.mynowinandroid.modules.interest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.weiran.mynowinandroid.modules.interest.InterestViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun InterestsRoute(viewModel: InterestViewModel = getViewModel()) {
    rememberCoroutineScope().launch { viewModel.observeData() }
    InterestScreen(viewModel)
}