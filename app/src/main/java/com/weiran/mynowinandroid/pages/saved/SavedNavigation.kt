package com.weiran.mynowinandroid.pages.saved

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.weiran.mynowinandroid.pages.saved.ui.SavedRoute

const val savedRoute = "saved_route"

fun NavController.navigateToSaved(navOptions: NavOptions? = null) {
    this.navigate(savedRoute, navOptions)
}

fun NavGraphBuilder.savedScreen() {
    composable(route = savedRoute) {
        SavedRoute()
    }
}