package com.weiran.mynowinandroid.store.data.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.weiran.mynowinandroid.ui.theme.MyIcons

data class TopicItem(
    val name: String,
    val id: String,
    val selected: Boolean = false,
    val imageUrl: String = "",
    val icon: ImageVector = MyIcons.Add,
)