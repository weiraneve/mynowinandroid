package com.weiran.mynowinandroid.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.weiran.mynowinandroid.viewmodel.HomeAction
import com.weiran.mynowinandroid.viewmodel.HomeTabs

@Composable
fun MyNavigationBar(
    tabs: Array<HomeTabs>,
    currentTab: HomeTabs,
    dispatchAction: (action: HomeAction) -> Unit
) {

    NavigationBar {
        tabs.forEach {
            NavigationBarItem(
                selected = it == currentTab,
                onClick = {
                    dispatchAction(HomeAction.CurrentTabChangedAction(it))
                },
                icon = {
                    if (it == currentTab) {
                        Icon(
                            painter = painterResource(id = it.selectIcon),
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