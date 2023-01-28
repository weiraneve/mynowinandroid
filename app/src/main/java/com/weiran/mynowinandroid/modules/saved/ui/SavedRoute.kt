package com.weiran.mynowinandroid.modules.saved.ui

import androidx.compose.runtime.Composable
import com.weiran.mynowinandroid.modules.saved.SavedViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SavedRoute(viewModel: SavedViewModel = getViewModel()) {
    SavedScreen(viewModel)
}