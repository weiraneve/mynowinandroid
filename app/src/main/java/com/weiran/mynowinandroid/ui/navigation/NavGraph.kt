package com.weiran.mynowinandroid.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weiran.mynowinandroid.pages.foryou.ui.ForYouRoute
import com.weiran.mynowinandroid.pages.interest.ui.InterestsRoute
import com.weiran.mynowinandroid.pages.saved.ui.SavedRoute
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.FOR_YOU_ROUTE
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.INTEREST_ROUTE
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.SAVED_ROUTE


fun NavGraphBuilder.forYouScreen() {
    composable(route = FOR_YOU_ROUTE) {
        ForYouRoute()
    }
}

fun NavGraphBuilder.savedScreen() {
    composable(route = SAVED_ROUTE) {
        SavedRoute()
    }
}

fun NavGraphBuilder.interestScreen() {
    composable(route = INTEREST_ROUTE) {
        InterestsRoute()
    }
}

fun NavController.navigateToForYou(navOptions: NavOptions? = null) {
    this.navigate(FOR_YOU_ROUTE, navOptions)
}


fun NavController.navigateToSaved(navOptions: NavOptions? = null) {
    this.navigate(SAVED_ROUTE, navOptions)
}


fun NavController.navigateToInterest(navOptions: NavOptions? = null) {
    this.navigate(INTEREST_ROUTE, navOptions)
}

object NavDestinations {
    const val FOR_YOU_ROUTE = "for_you_route"
    const val SAVED_ROUTE = "saved_route"
    const val INTEREST_ROUTE = "interest_route"
}