package com.weiran.mynowinandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.weiran.mynowinandroid.modules.foryou.forYouRoute
import com.weiran.mynowinandroid.modules.foryou.forYouScreen
import com.weiran.mynowinandroid.modules.interest.interestScreen
import com.weiran.mynowinandroid.modules.saved.savedScreen

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun MyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = forYouRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        forYouScreen()
        savedScreen()
        interestScreen()
    }
}