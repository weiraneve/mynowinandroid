package com.weiran.mynowinandroid.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.weiran.mynowinandroid.home.HomeAction
import com.weiran.mynowinandroid.home.TabState

@Composable
fun MyNavigationBar(
    dispatchAction: (action: HomeAction) -> Unit,
    tabStates: List<TabState>
) {

    NavigationBar {
        tabStates.forEach {
            NavigationBarItem(
                selected = it.isSelected,
                onClick = {
                    dispatchAction(HomeAction.ChangeCurrentTabAction(it))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(it.title)) }
            )
        }
    }
}