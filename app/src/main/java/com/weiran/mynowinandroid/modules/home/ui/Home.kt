package com.weiran.mynowinandroid.modules.home.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.common.utils.NetworkMonitor
import com.weiran.mynowinandroid.modules.home.HomeViewModel
import com.weiran.mynowinandroid.ui.component.MyNavigationBar
import com.weiran.mynowinandroid.ui.component.MySettingsDialog
import com.weiran.mynowinandroid.ui.component.MyTopBar
import com.weiran.mynowinandroid.ui.navigation.MyNavHost
import com.weiran.mynowinandroid.ui.theme.Material
import com.weiran.mynowinandroid.ui.theme.MyIcons

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Home(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    appState: HomeState = rememberMyAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass
    )
) {
    val viewModel: HomeViewModel = viewModel()
    val state = viewModel.homeState.collectAsStateWithLifecycle().value
    val onAction = viewModel::onAction
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            val destination = appState.currentTopLevelDestination
            if (destination != null) {
                MyTopBar(
                    modifier = Modifier.zIndex(Material.groundIndex),
                    title = destination.titleTextId,
                    actionIcon = MyIcons.Settings,
                    onActionClick = { appState::setShowSettingsDialog.invoke(true) }
                )
            }
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                MyNavigationBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (appState.shouldShowSettingsDialog) {
            MySettingsDialog(
                themeState = state.settingThemeState,
                dispatchAction = onAction,
                onDismiss = { appState::setShowSettingsDialog.invoke(false) }
            )
        }

        val isOffline by appState.isOffline.collectAsStateWithLifecycle()
        val notConnected = stringResource(R.string.not_connected)

        LaunchedEffect(isOffline) {
            if (isOffline) snackbarHostState.showSnackbar(
                message = notConnected,
                duration = SnackbarDuration.Indefinite
            )
        }
        MyNavHost(
            navController = appState.navController,
            modifier = Modifier
                .padding(padding)
                .consumedWindowInsets(padding)
        )
    }
}