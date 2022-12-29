package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.ui.component.InterestItem
import com.weiran.mynowinandroid.viewmodel.ForYouAction
import com.weiran.mynowinandroid.viewmodel.ForYouViewModel

@Composable
fun InterestScreen() {

    val viewModel: ForYouViewModel = viewModel()
    val topicItems = viewModel.forYouState.collectAsState().value.topicItems

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp),
    ) {
        topicItems.forEach { topicItem ->
            item {
                InterestItem(
                    name = topicItem.name,
                    selected = topicItem.selected,
                    topicIcon = topicItem.icon,
                    onClick = { viewModel::dispatchAction.invoke(ForYouAction.TopicClickAction(topicItem.id)) },
                    onCheckedChange = { viewModel::dispatchAction.invoke(ForYouAction.TopicClickAction(topicItem.id)) }
                )
            }
        }
    }
}