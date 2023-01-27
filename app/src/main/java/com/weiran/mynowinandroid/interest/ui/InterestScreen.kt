package com.weiran.mynowinandroid.interest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weiran.mynowinandroid.component.InterestItem
import com.weiran.mynowinandroid.component.MyOverlayLoadingWheel
import com.weiran.mynowinandroid.foryou.FeedUIState
import com.weiran.mynowinandroid.interest.InterestAction
import com.weiran.mynowinandroid.interest.InterestViewModel
import com.weiran.mynowinandroid.theme.Dimensions

@Composable
fun InterestScreen(viewModel: InterestViewModel) {
    LaunchedEffect(Unit) { viewModel.observeData() }
    val state = viewModel.interestState.collectAsStateWithLifecycle().value
    val topicItems = state.topicItems
    val action = viewModel::dispatchAction
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