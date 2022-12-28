package com.weiran.mynowinandroid.ui.component

import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun MyIconToggleButton(
    selected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    FilledIconToggleButton(
        checked = selected,
        onCheckedChange = onCheckedChange,
    ) {
        if (selected) {
            Icon(
                imageVector = MyIcons.Check,
                contentDescription = null
            )
        } else {
            Icon(
                imageVector = MyIcons.Add,
                contentDescription = null
            )
        }
    }
}