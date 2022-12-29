package com.weiran.mynowinandroid.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.viewmodel.ForYouAction

@Composable
fun TopicSelection(
    modifier: Modifier = Modifier,
    topicItems: List<TopicItem>,
    dispatchAction: (action: ForYouAction) -> Unit,
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(24.dp),
        modifier = modifier
            .heightIn(max = max(240.dp, with(LocalDensity.current) { 240.sp.toDp() }))
            .fillMaxWidth()
    ) {
        items(topicItems.size) { index ->
            TopicItem(
                name = topicItems[index].name,
                selected = topicItems[index].selected,
                topicIcon = topicItems[index].icon,
                onClick = { dispatchAction.invoke(ForYouAction.TopicClickAction(topicItems[index].id)) },
                onCheckedChange = { dispatchAction.invoke(ForYouAction.TopicClickAction(topicItems[index].id)) },
            )
        }
    }
}