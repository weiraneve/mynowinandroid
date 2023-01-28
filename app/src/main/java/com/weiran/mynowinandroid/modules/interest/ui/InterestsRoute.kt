package com.weiran.mynowinandroid.modules.interest.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.weiran.mynowinandroid.modules.interest.InterestViewModel

@Composable
fun InterestsRoute(viewModel: InterestViewModel = hiltViewModel()) {
    InterestScreen(viewModel)
}