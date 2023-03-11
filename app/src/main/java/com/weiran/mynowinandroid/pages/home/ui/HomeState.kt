package com.weiran.mynowinandroid.pages.home.ui

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.weiran.mynowinandroid.common.utils.NetworkMonitor
import com.weiran.mynowinandroid.ui.navigation.NavDestinations
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination.FOR_YOU
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination.INTERESTS
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination.SAVED
import com.weiran.mynowinandroid.ui.navigation.navigateToForYou
import com.weiran.mynowinandroid.ui.navigation.navigateToInterests
import com.weiran.mynowinandroid.ui.navigation.navigateToSaved
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberMyAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    customCoroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController()
): HomeState {
    return remember(navController, customCoroutineScope, windowSizeClass, networkMonitor) {
        HomeState(
            navController = navController,
            coroutineScope = customCoroutineScope,
            windowSizeClass = windowSizeClass,
            networkMonitor = networkMonitor
        )
    }
}

@Stable
class HomeState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            NavDestinations.FOR_YOU_ROUTE -> FOR_YOU
            NavDestinations.SAVED_ROUTE -> SAVED
            NavDestinations.INTERESTS_ROUTE -> INTERESTS
            else -> null
        }

    var shouldShowSettingsDialog by mutableStateOf(false)
        private set

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact ||
                windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            FOR_YOU -> navController.navigateToForYou(topLevelNavOptions)
            SAVED -> navController.navigateToSaved(topLevelNavOptions)
            INTERESTS -> navController.navigateToInterests(topLevelNavOptions)
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }

    fun setShowSettingsDialog(shouldShow: Boolean) {
        shouldShowSettingsDialog = shouldShow
    }

}