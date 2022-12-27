package com.weiran.mynowinandroid.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.weiran.mynowinandroid.ui.home.HomeTabs
import com.weiran.mynowinandroid.viewmodel.HomeAction

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