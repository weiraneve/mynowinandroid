package com.weiran.mynowinandroid.modules.home.ui

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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.weiran.mynowinandroid.modules.foryou.forYouRoute
import com.weiran.mynowinandroid.modules.foryou.navigateToForYou
import com.weiran.mynowinandroid.modules.interest.interestRoute
import com.weiran.mynowinandroid.modules.interest.navigateToInterest
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination.FOR_YOU
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination.INTERESTS
import com.weiran.mynowinandroid.ui.navigation.TopLevelDestination.SAVED
import com.weiran.mynowinandroid.ui.navigation.TrackDisposableJank
import com.weiran.mynowinandroid.modules.saved.navigateToSaved
import com.weiran.mynowinandroid.modules.saved.savedRoute
import com.weiran.mynowinandroid.common.utils.NetworkMonitor
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
    NavigationTrackingSideEffect(navController)
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
            forYouRoute -> FOR_YOU
            savedRoute -> SAVED
            interestRoute -> INTERESTS
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

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                FOR_YOU -> navController.navigateToForYou(topLevelNavOptions)
                SAVED -> navController.navigateToSaved(topLevelNavOptions)
                INTERESTS -> navController.navigateToInterest(topLevelNavOptions)
            }
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }

    fun setShowSettingsDialog(shouldShow: Boolean) {
        shouldShowSettingsDialog = shouldShow
    }

}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}