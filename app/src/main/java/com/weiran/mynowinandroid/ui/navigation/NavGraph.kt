package com.weiran.mynowinandroid.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.FOR_YOU_ROUTE
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.INTEREST_ROUTE
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.SAVED_ROUTE

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
    const val WEB_ROUTE = "web"
}