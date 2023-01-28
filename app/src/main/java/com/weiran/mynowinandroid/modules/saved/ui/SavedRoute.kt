package com.weiran.mynowinandroid.modules.saved.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.weiran.mynowinandroid.modules.saved.SavedViewModel

@Composable
fun SavedRoute(viewModel: SavedViewModel = hiltViewModel()) {
    SavedScreen(viewModel)
}