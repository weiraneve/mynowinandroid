package com.weiran.mynowinandroid.interest.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.component.InterestItem
import com.weiran.mynowinandroid.foryou.ForYouAction
import com.weiran.mynowinandroid.foryou.ForYouViewModel
import com.weiran.mynowinandroid.theme.Dimensions

@Composable
fun InterestScreen() {
    val forYouViewModel: ForYouViewModel = viewModel()
    val topicItems = forYouViewModel.forYouState.collectAsState().value.topicItems
    val dispatchAction = forYouViewModel::dispatchAction

    LazyColumn(modifier = Modifier.padding(horizontal = Dimensions.standardSpacing)) {
        topicItems.forEach {
            item(it.id) {
                InterestItem(
                    name = it.name,
                    selected = it.selected,
                    topicIcon = it.icon,
                    imageUrl = it.imageUrl,
                    onCheckedChange = { dispatchAction(ForYouAction.TopicSelected(it.id)) }
                )
            }
        }
    }
}