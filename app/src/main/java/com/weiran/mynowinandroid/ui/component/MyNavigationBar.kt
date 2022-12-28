package com.weiran.mynowinandroid.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.weiran.mynowinandroid.viewmodel.HomeAction
import com.weiran.mynowinandroid.viewmodel.TabState

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
                    dispatchAction(HomeAction.CurrentTabChangedAction(it))
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