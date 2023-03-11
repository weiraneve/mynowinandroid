package com.weiran.mynowinandroid.ui.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.FOR_YOU_ROUTE
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.INTERESTS_ROUTE
import com.weiran.mynowinandroid.ui.navigation.NavDestinations.SAVED_ROUTE

fun NavController.navigateToForYou(navOptions: NavOptions? = null) {
    this.navigate(FOR_YOU_ROUTE, navOptions)
}

fun NavController.navigateToSaved(navOptions: NavOptions? = null) {
    this.navigate(SAVED_ROUTE, navOptions)
}

fun NavController.navigateToInterests(navOptions: NavOptions? = null) {
    this.navigate(INTERESTS_ROUTE, navOptions)
}

fun NavController.navigateToWeb(url: String) {
    val navOptions = navOptions { launchSingleTop = true }
    this.navigate(NavDestinations.WEB_ROUTE + "/" + Uri.encode(url), navOptions)
}

object NavDestinations {
    const val FOR_YOU_ROUTE = "for_you_route"
    const val SAVED_ROUTE = "saved_route"
    const val INTERESTS_ROUTE = "interests_route"
    const val WEB_ROUTE = "web"
}