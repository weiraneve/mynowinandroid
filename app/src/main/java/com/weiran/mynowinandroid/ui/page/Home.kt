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
import com.weiran.mynowinandroid.ui.theme.MyIcons
import com.weiran.mynowinandroid.ui.component.MyNavigationBar
import com.weiran.mynowinandroid.ui.component.MyTopBar
import com.weiran.mynowinandroid.ui.theme.Material
import com.weiran.mynowinandroid.viewmodel.HomeTab
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
                modifier = Modifier.zIndex(Material.groundIndex),
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
                HomeTab.FOR_PAGE -> ForYouScreen()
                HomeTab.SAVED_PAGE -> SavedScreen()
                HomeTab.INTEREST_PAGE -> InterestScreen()
            }
        }
    }

}