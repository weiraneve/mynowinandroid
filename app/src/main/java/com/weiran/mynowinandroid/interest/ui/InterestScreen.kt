package com.weiran.mynowinandroid.interest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.component.InterestItem
import com.weiran.mynowinandroid.component.MyOverlayLoadingWheel
import com.weiran.mynowinandroid.foryou.FeedUIState
import com.weiran.mynowinandroid.interest.InterestAction
import com.weiran.mynowinandroid.interest.InterestViewModel
import com.weiran.mynowinandroid.theme.Dimensions

@Composable
fun InterestScreen(viewModel: InterestViewModel = viewModel()) {
    val state = viewModel.interestState.collectAsState().value
    val topicItems = state.topicItems
    val action = viewModel::dispatchAction
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.observeData()
                }

                else -> {}
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }
    MyOverlayLoadingWheel(isFeedLoading = state.feedUIState is FeedUIState.Loading)
    LazyColumn(modifier = Modifier.padding(horizontal = Dimensions.standardSpacing)) {
        topicItems.forEach {
            item(it.id) {
                InterestItem(
                    name = it.name,
                    selected = it.selected,
                    topicIcon = it.icon,
                    imageUrl = it.imageUrl,
                    onCheckedChange = { action(InterestAction.TopicSelected(it.id)) }
                )
            }
        }
    }
}