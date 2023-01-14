package com.weiran.mynowinandroid.interest.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.weiran.mynowinandroid.interest.InterestViewModel

@Composable
fun InterestsRoute(viewModel: InterestViewModel = hiltViewModel()) {
    InterestScreen(viewModel)
}