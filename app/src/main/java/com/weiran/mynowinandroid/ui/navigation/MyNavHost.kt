package com.weiran.mynowinandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weiran.mynowinandroid.ui.pages.foryou.ui.ForYouRoute
import com.weiran.mynowinandroid.ui.pages.interest.ui.InterestsRoute
import com.weiran.mynowinandroid.ui.pages.saved.ui.SavedRoute
import com.weiran.mynowinandroid.ui.pages.web.WebScreen

@Composable
fun MyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onNavigateToWeb: (url: String) -> Unit = {},
    startDestination: String = NavDestinations.FOR_YOU_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = NavDestinations.FOR_YOU_ROUTE) {
            ForYouRoute(onNavigateToWeb)
        }
        composable(route = NavDestinations.SAVED_ROUTE) {
            SavedRoute()
        }

        composable(route = NavDestinations.INTERESTS_ROUTE) {
            InterestsRoute()
        }

        composable(route = "${NavDestinations.FOR_YOU_ROUTE}/${NavDestinations.WEB_ROUTE}/{url}") { backStackEntry ->
            WebScreen(url = backStackEntry.arguments?.getString("url") ?: "")
        }

    }
}
