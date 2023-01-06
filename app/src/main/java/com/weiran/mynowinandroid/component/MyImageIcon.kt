package com.weiran.mynowinandroid.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import coil.compose.AsyncImage

@Composable
fun MyImageIcon(
    imageUrl: String,
    modifier: Modifier,
    errorPlaceholder: Painter? = null,
    placeholder: Painter? = null
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        placeholder = placeholder,
        fallback = errorPlaceholder,
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
        modifier = modifier
    )
}