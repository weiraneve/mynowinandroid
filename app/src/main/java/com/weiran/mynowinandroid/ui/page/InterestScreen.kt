package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.ui.component.InterestItem
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.viewmodel.FeedViewModel
import com.weiran.mynowinandroid.viewmodel.FeedAction

@Composable
fun InterestScreen() {

    val feedViewModel: FeedViewModel = viewModel()
    val topicItems = feedViewModel.feedState.collectAsState().value.topicItems
    val dispatchAction = feedViewModel::dispatchAction

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = Dimensions.standardSpacing),
    ) {
        topicItems.forEach {
            item {
                InterestItem(
                    name = it.name,
                    selected = it.selected,
                    topicIcon = it.icon,
                    onCheckedChange = { dispatchAction(FeedAction.FeedSelected(it.id)) }
                )
            }
        }
    }
}