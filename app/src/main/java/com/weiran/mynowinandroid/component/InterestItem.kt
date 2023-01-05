package com.weiran.mynowinandroid.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.weiran.mynowinandroid.theme.Dimensions
import com.weiran.mynowinandroid.theme.Material

@Composable
fun InterestItem(
    name: String,
    selected: Boolean,
    imageUrl: String,
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
            MyImageIcon(imageUrl, Modifier.size(Dimensions.dimension44))
            Spacer(modifier = Modifier.width(Dimensions.standardSpacing))
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