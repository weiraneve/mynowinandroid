package com.weiran.mynowinandroid.ui.component

import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyIconToggleButton(
    selected: Boolean,
    topicIcon: ImageVector,
    onCheckedChange: (Boolean) -> Unit,
) {
    FilledIconToggleButton(checked = selected, onCheckedChange = onCheckedChange) {
        Icon(
            imageVector = topicIcon,
            contentDescription = null
        )
    }
}