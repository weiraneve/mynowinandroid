package com.weiran.mynowinandroid.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.foryou.ForYouAction
import com.weiran.mynowinandroid.theme.Dimensions
import com.weiran.mynowinandroid.theme.Material

@Composable
fun TopicSection(
    modifier: Modifier = Modifier,
    topicItems: List<TopicItem>,
    dispatchAction: (action: ForYouAction) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(Material.cellRows),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.standardSpacing),
        verticalArrangement = Arrangement.spacedBy(Dimensions.standardSpacing),
        contentPadding = PaddingValues(Dimensions.standardPadding),
        modifier = modifier
            .heightIn(max = Dimensions.heightInMax)
            .fillMaxWidth()
    ) {
        topicItems.forEach {
            item {
                TopicItem(
                    name = it.name,
                    selected = it.selected,
                    imageUrl = it.imageUrl,
                    topicIcon = it.icon,
                    onCheckedChange = { dispatchAction.invoke(ForYouAction.TopicSelected(it.id)) }
                )
            }
        }
    }
}