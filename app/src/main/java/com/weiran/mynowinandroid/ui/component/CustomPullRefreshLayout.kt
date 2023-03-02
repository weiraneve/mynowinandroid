package com.weiran.mynowinandroid.ui.component

import androidx.compose.animation.core.animate
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow

@Composable
@ExperimentalMaterialApi
fun rememberPullRefreshLayoutState(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    refreshThreshold: Dp = PullRefreshDefaults.RefreshThreshold,
    refreshingOffset: Dp = PullRefreshDefaults.RefreshingOffset,
): PullRefreshLayoutState {
    require(refreshThreshold > 0.dp) { "The refresh trigger must be greater than zero!" }

    val scope = rememberCoroutineScope()
    val onRefreshState = rememberUpdatedState(onRefresh)
    val thresholdPx: Float
    val refreshingOffsetPx: Float

    with(LocalDensity.current) {
        thresholdPx = refreshThreshold.toPx()
        refreshingOffsetPx = refreshingOffset.toPx()
    }

    val state = remember(scope) {
        PullRefreshLayoutState(scope, onRefreshState, refreshingOffsetPx, thresholdPx)
    }

    SideEffect { state.setRefreshing(refreshing) }

    return state
}

@ExperimentalMaterialApi
fun Modifier.pullRefreshLayout(
    state: PullRefreshLayoutState,
    enabled: Boolean = true
) = inspectable(inspectorInfo = debugInspectorInfo {
    name = "pullRefresh"
    properties["state"] = state
    properties["enabled"] = enabled
}) {
    Modifier.pullRefresh(state::onPull, { state.onRelease() }, enabled)
}

@ExperimentalMaterialApi
class PullRefreshLayoutState internal constructor(
    private val animationScope: CoroutineScope,
    private val onRefreshState: State<() -> Unit>,
    private val refreshingOffset: Float,
    private val threshold: Float
) {

    private val progress get() = adjustedDistancePulled / threshold
    val position get() = _position

    private val adjustedDistancePulled by derivedStateOf { distancePulled * 0.5f }
    private var _refreshing by mutableStateOf(false)
    private var _position by mutableStateOf(0f)
    private var distancePulled by mutableStateOf(0f)

    internal fun onPull(pullDelta: Float): Float {
        if (this._refreshing) return 0f

        val newOffset = (distancePulled + pullDelta).coerceAtLeast(0f)
        val dragConsumed = newOffset - distancePulled
        distancePulled = newOffset
        _position = calculateIndicatorPosition()
        return dragConsumed
    }

    internal fun onRelease() {
        if (!this._refreshing) {
            if (adjustedDistancePulled > threshold) {
                onRefreshState.value()
            } else {
                animateIndicatorTo(0f)
            }
        }
        distancePulled = 0f
    }

    internal fun setRefreshing(refreshing: Boolean) {
        if (this._refreshing != refreshing) {
            this._refreshing = refreshing
            this.distancePulled = 0f
            animateIndicatorTo(if (refreshing) refreshingOffset else 0f)
        }
    }

    private fun animateIndicatorTo(offset: Float) = animationScope.launch {
        animate(initialValue = _position, targetValue = offset) { value, _ ->
            _position = value
        }
    }

    private fun calculateIndicatorPosition(): Float = when {
        adjustedDistancePulled <= threshold -> adjustedDistancePulled
        else -> {
            val overshootPercent = Math.abs(progress) - 1.0f
            val linearTension = overshootPercent.coerceIn(0f, 2f)
            val tensionPercent = linearTension - linearTension.pow(2) / 4
            val extraOffset = threshold * tensionPercent
            threshold + extraOffset
        }
    }
}