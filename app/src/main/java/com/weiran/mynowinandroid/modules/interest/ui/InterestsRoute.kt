package com.weiran.mynowinandroid.modules.interest.ui

import androidx.compose.runtime.Composable
import com.weiran.mynowinandroid.modules.interest.InterestViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun InterestsRoute(viewModel: InterestViewModel = getViewModel()) {
    InterestScreen(viewModel)
}