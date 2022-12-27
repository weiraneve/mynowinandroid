package com.weiran.mynowinandroid.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.ui.component.MyNavigationBar
import com.weiran.mynowinandroid.ui.page.ForYouScreen
import com.weiran.mynowinandroid.ui.page.InterestScreen
import com.weiran.mynowinandroid.ui.page.SavedScreen
import com.weiran.mynowinandroid.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {

    val viewModel: HomeViewModel = viewModel()
    val position by viewModel.position.collectAsState()
    val tabs = HomeTabs.values()
    val dispatchAction = viewModel::dispatchAction

    Scaffold(
        bottomBar = {
            MyNavigationBar(tabs, position, dispatchAction)
        }
    ) { innerPadding ->
        Modifier.padding(innerPadding)
        when (position) {
            HomeTabs.FOR_PAGE -> ForYouScreen()
            HomeTabs.SAVED_PAGE -> SavedScreen()
            HomeTabs.INTEREST_PAGE -> InterestScreen()
        }
    }

}