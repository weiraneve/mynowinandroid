package com.weiran.mynowinandroid.pages.interest

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weiran.mynowinandroid.pages.interest.ui.InterestsRoute

const val interestRoute = "interest_route"

fun NavController.navigateToInterest(navOptions: NavOptions? = null) {
    this.navigate(interestRoute, navOptions)
}

fun NavGraphBuilder.interestScreen() {
    composable(route = interestRoute) {
        InterestsRoute()
    }
}
