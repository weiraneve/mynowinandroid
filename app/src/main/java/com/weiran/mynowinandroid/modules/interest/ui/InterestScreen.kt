package com.weiran.mynowinandroid.modules.interest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weiran.mynowinandroid.modules.foryou.FeedUIState
import com.weiran.mynowinandroid.modules.interest.InterestAction
import com.weiran.mynowinandroid.modules.interest.InterestViewModel
import com.weiran.mynowinandroid.ui.component.InterestItem
import com.weiran.mynowinandroid.ui.component.MyOverlayLoadingWheel
import com.weiran.mynowinandroid.ui.component.rememberPullRefreshLayoutState
import com.weiran.mynowinandroid.ui.theme.Dimensions

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InterestScreen(viewModel: InterestViewModel) {
    val state = viewModel.interestState.collectAsStateWithLifecycle().value
    val topicItems = state.topicItems
    val action = viewModel::onAction
    val refreshing = state.feedUIState is FeedUIState.Loading

    val pullState = rememberPullRefreshLayoutState(
        refreshing = refreshing,
        onRefresh = {}
    )
    MyOverlayLoadingWheel(refreshing = refreshing, pullState = pullState)
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