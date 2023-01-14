package com.weiran.mynowinandroid.home.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.component.MySettingsDialog
import com.weiran.mynowinandroid.component.MyTopBar
import com.weiran.mynowinandroid.home.HomeViewModel
import com.weiran.mynowinandroid.navigation.NiaNavHost
import com.weiran.mynowinandroid.navigation.TopLevelDestination
import com.weiran.mynowinandroid.theme.Material
import com.weiran.mynowinandroid.theme.MyIcons
import com.weiran.mynowinandroid.utils.NetworkMonitor

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
    val state = viewModel.homeState.collectAsState().value
    val dispatchAction = viewModel::dispatchAction
    val showSettingsDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            val destination = appState.currentTopLevelDestination
            if (destination != null) {
                MyTopBar(
                    modifier = Modifier.zIndex(Material.groundIndex),
                    title = destination.titleTextId,
                    actionIcon = MyIcons.Settings,
                    onActionClick = { showSettingsDialog.value = true }
                )
            }
        },
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                NiaBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState.currentDestination
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

//        Box(modifier = Modifier.padding(padding)) {
            // todo move showSettingsDialog to viewmodel
            if (showSettingsDialog.value) {
                MySettingsDialog(
                    themeState = state.settingTheme,
                    dispatchAction = dispatchAction,
                    onDismiss = { showSettingsDialog.value = false }
                )
            }

            val isOffline by appState.isOffline.collectAsStateWithLifecycle()
            // If user is not connected to the internet show a snack bar to inform them.
            val notConnected = stringResource(R.string.not_connected)
            LaunchedEffect(isOffline) {
                if (isOffline) snackbarHostState.showSnackbar(
                    message = notConnected,
                    duration = SnackbarDuration.Indefinite
                )
            }
            NiaNavHost(
                navController = appState.navController,
                modifier = Modifier
                    .padding(padding)
                    .consumedWindowInsets(padding)
            )
        }
}

@Composable
private fun NiaBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NiaNavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NiaNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) }
            )
        }
    }
}

@Composable
fun NiaNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    NavigationBar(
        modifier = modifier,
        content = content
    )
}

@Composable
fun RowScope.NiaNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel
    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false