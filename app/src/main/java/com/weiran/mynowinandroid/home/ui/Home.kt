package com.weiran.mynowinandroid.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.component.MyNavigationBar
import com.weiran.mynowinandroid.component.MySettingsDialog
import com.weiran.mynowinandroid.component.MyTopBar
import com.weiran.mynowinandroid.foryou.ui.ForYouScreen
import com.weiran.mynowinandroid.home.HomeTab
import com.weiran.mynowinandroid.home.HomeViewModel
import com.weiran.mynowinandroid.interest.ui.InterestScreen
import com.weiran.mynowinandroid.saved.ui.SavedScreen
import com.weiran.mynowinandroid.theme.Material
import com.weiran.mynowinandroid.theme.MyIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val viewModel: HomeViewModel = viewModel()
    val state = viewModel.homeState.collectAsState().value
    val currentTab = state.currentTab
    val tabStates = state.tabStates
    val dispatchAction = viewModel::dispatchAction
    val showSettingsDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MyTopBar(
                modifier = Modifier.zIndex(Material.groundIndex),
                title = currentTab.title,
                actionIcon = MyIcons.Settings,
                onActionClick = { showSettingsDialog.value = true }
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
        // todo move showSettingsDialog to viewmodel
        if (showSettingsDialog.value) {
            MySettingsDialog(
                themeState = state.settingTheme,
                dispatchAction = dispatchAction,
                onDismiss = { showSettingsDialog.value = false }
            )
        }
    }
}