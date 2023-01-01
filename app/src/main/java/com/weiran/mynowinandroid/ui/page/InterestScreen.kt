package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.ui.component.InterestItem
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.viewmodel.TopicAction
import com.weiran.mynowinandroid.viewmodel.TopicViewModel

@Composable
fun InterestScreen() {

    val viewModel: TopicViewModel = viewModel()
    val topicItems = viewModel.topicState.collectAsState().value.topicItems

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
                    onCheckedChange = { viewModel::dispatchAction.invoke(TopicAction.TopicSelected(it.id)) }
                )
            }
        }
    }
}