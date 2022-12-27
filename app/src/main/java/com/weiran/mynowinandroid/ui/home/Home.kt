package com.weiran.mynowinandroid.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.viewmodel.HomeAction
import com.weiran.mynowinandroid.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {

    val viewModel: HomeViewModel = viewModel()
    val position by viewModel.position.collectAsState()
    val tabs = HomeTabs.values()
    val dispatchAction = viewModel::dispatchAction

    Scaffold(
        bottomBar = {
            MyNavigationBar(tabs, position, dispatchAction)
        }
    ) { innerPadding ->
        Modifier.padding(innerPadding)
        when (position) {
            HomeTabs.FOR_PAGE -> HomePage()
            HomeTabs.SAVED_PAGE -> SavedPage()
            HomeTabs.INTEREST_PAGE -> InterestPage()
        }
    }

}

@Composable
fun MyNavigationBar(
    tabs: Array<HomeTabs>,
    position: HomeTabs,
    dispatchAction: (action: HomeAction) -> Unit
) {

    NavigationBar {
        tabs.forEach {
            NavigationBarItem(
                selected = it == position,
                onClick = {
                    dispatchAction(HomeAction.PositionChangedAction(it))
                },
                icon = {
                    if (it == position) {
                        Icon(
                            painter = painterResource(id = it.clickIcon),
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(stringResource(it.title)) }
            )
        }
    }
}