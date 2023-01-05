package com.weiran.mynowinandroid.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.theme.Dimensions
import com.weiran.mynowinandroid.theme.Material
import com.weiran.mynowinandroid.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicItem(
    name: String,
    selected: Boolean,
    imageUrl: String,
    topicIcon: ImageVector,
    onCheckedChange: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(Dimensions.surfaceWidth)
            .heightIn(min = Dimensions.heightInMin),
        shape = Shapes.large,
        selected = selected,
        onClick = { onCheckedChange() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = Dimensions.standardPadding)
        ) {
            TopicIcon(imageUrl = imageUrl)
            Text(
                text = name,
                modifier = Modifier
                    .padding(horizontal = Dimensions.standardPadding)
                    .weight(Material.fullWeight),
                color = MaterialTheme.colorScheme.onSurface
            )
            MyIconToggleButton(
                selected = selected,
                topicIcon = topicIcon,
                onCheckedChange = { onCheckedChange() }
            )
        }
    }
}

@Composable
fun TopicIcon(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    MyImageIcon(
        placeholder = painterResource(R.drawable.ic_icon_placeholder),
        imageUrl = imageUrl,
        modifier = modifier
            .padding(Dimensions.dimension12)
            .size(Dimensions.dimension32)
    )
}