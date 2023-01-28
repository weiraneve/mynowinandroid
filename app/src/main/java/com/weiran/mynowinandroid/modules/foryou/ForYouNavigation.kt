package com.weiran.mynowinandroid.modules.foryou

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weiran.mynowinandroid.modules.foryou.ui.ForYouRoute

const val forYouRoute = "for_you_route"

fun NavController.navigateToForYou(navOptions: NavOptions? = null) {
    this.navigate(forYouRoute, navOptions)
}

fun NavGraphBuilder.forYouScreen() {
    composable(route = forYouRoute) {
        ForYouRoute()
    }
}
