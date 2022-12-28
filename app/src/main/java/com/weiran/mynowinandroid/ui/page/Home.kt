package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.ui.component.MyIcons
import com.weiran.mynowinandroid.ui.component.MyNavigationBar
import com.weiran.mynowinandroid.ui.component.MyTopBar
import com.weiran.mynowinandroid.viewmodel.HomeTabs
import com.weiran.mynowinandroid.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {

    val viewModel: HomeViewModel = viewModel()
    val state = viewModel.homeState.collectAsState().value
    val currentTab = state.currentTab
    val tabStates = state.tabStates
    val dispatchAction = viewModel::dispatchAction

    Scaffold(
        topBar = {
            MyTopBar(
                modifier = Modifier.zIndex(-1F),
                title = currentTab.title,
                actionIcon = MyIcons.Settings,
            )
        },
        bottomBar = {
            MyNavigationBar(dispatchAction, tabStates)
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentTab) {
                HomeTabs.FOR_PAGE -> ForYouScreen()
                HomeTabs.SAVED_PAGE -> SavedScreen()
                HomeTabs.INTEREST_PAGE -> InterestScreen()
            }
        }
    }

}