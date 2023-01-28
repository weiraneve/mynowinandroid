package com.weiran.mynowinandroid.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable

@Immutable
object Shapes {
    val small: CornerBasedShape = RoundedCornerShape(Dimensions.dimension2)
    val medium: CornerBasedShape = RoundedCornerShape(Dimensions.dimension4)
    val large: CornerBasedShape = RoundedCornerShape(Dimensions.dimension8)
}