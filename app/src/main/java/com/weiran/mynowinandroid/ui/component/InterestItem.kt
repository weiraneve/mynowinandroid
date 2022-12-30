package com.weiran.mynowinandroid.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.ui.theme.Material

@Composable
fun InterestItem(
    name: String,
    selected: Boolean,
    topicIcon: ImageVector,
    onCheckedChange: () -> Unit
) {
    Row {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(Material.fullWeight)
                .clickable { onCheckedChange() }
                .padding(vertical = Dimensions.dimension12)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        MyIconToggleButton(
            selected = selected,
            topicIcon = topicIcon,
            onCheckedChange = { onCheckedChange() }
        )
    }
}