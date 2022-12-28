package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.ui.component.InterestItem
import com.weiran.mynowinandroid.viewmodel.ForYouViewModel

@Composable
fun InterestScreen() {

    val viewModel: ForYouViewModel = viewModel()
    val topics = viewModel.forYouState.collectAsState().value.topics

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp),
    ) {
        topics.forEach { topic ->
            item {
                InterestItem(
                    name = topic.name,
                    selected = topic.followed,
                    onClick = {}
                )
            }
        }
    }
}