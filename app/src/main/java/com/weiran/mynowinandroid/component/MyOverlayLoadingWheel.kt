package com.weiran.mynowinandroid.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.theme.Dimensions
import kotlinx.coroutines.launch


@Composable
fun MyOverlayLoadingWheel(
    isFeedLoading: Boolean
) {
    AnimatedVisibility(
        visible = isFeedLoading,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
        ) + fadeOut(),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                shape = RoundedCornerShape(Dimensions.dimension56),
                shadowElevation = Dimensions.dimension8,
                modifier = Modifier
                    .size(Dimensions.dimension56)
                    .align(Alignment.Center),
            ) {
                MyLoadingWheel(text = stringResource(id = R.string.for_you_loading))
            }
        }
    }
}

@Composable
fun MyLoadingWheel(
    text: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val startValue = if (LocalInspectionMode.current) VOID else FULL
    val floatAnimValues = (0 until NUM_OF_LINES).map { remember { Animatable(startValue) } }
    LaunchedEffect(floatAnimValues) {
        (0 until NUM_OF_LINES).map { index ->
            launch {
                floatAnimValues[index].animateTo(
                    targetValue = VOID,
                    animationSpec = tween(
                        durationMillis = MILLIS_100,
                        easing = FastOutSlowInEasing,
                        delayMillis = MILLIS_40 * index
                    )
                )
            }
        }
    }

    val rotationAnim by infiniteTransition.animateFloat(
        initialValue = VOID,
        targetValue = TARGET_TIME,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ROTATION_TIME, easing = LinearEasing)
        )
    )

    val baseLineColor = MaterialTheme.colorScheme.onBackground
    val progressLineColor = MaterialTheme.colorScheme.inversePrimary
    val colorAnimValues = (0 until NUM_OF_LINES).map { index ->
        infiniteTransition.animateColor(
            initialValue = baseLineColor,
            targetValue = baseLineColor,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = ROTATION_TIME / 2
                    progressLineColor at ROTATION_TIME / NUM_OF_LINES / 2 with LinearEasing
                    baseLineColor at ROTATION_TIME / NUM_OF_LINES with LinearEasing
                },
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(ROTATION_TIME / NUM_OF_LINES / 2 * index)
            )
        )
    }

    Canvas(
        modifier = modifier
            .size(Dimensions.dimension48)
            .padding(Dimensions.dimension8)
            .graphicsLayer { rotationZ = rotationAnim }
            .semantics { contentDescription = text }
    ) {
        repeat(NUM_OF_LINES) { index ->
            rotate(degrees = index * ROTATE_VALUE) {
                drawLine(
                    color = colorAnimValues[index].value,
                    alpha = if (floatAnimValues[index].value < ZERO_PIXEL) VOID else FULL,
                    strokeWidth = STROKE_WIDTH,
                    cap = StrokeCap.Round,
                    start = Offset(size.width / 2, size.height / 4),
                    end = Offset(size.width / 2, floatAnimValues[index].value * size.height / 4)
                )
            }
        }
    }
}

private const val ROTATION_TIME = 12000
private const val NUM_OF_LINES = 12
private const val MILLIS_40 = 40
private const val MILLIS_100 = 100
private const val TARGET_TIME = 360F
private const val ROTATE_VALUE = 30F
private const val ZERO_PIXEL = 0F
private const val VOID = 0F
private const val FULL = 1F
private const val STROKE_WIDTH = 4F