package com.weiran.mynowinandroid.ui.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.store.data.model.NewsItem
import com.weiran.mynowinandroid.store.data.model.TopicItem
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.ui.theme.Material
import com.weiran.mynowinandroid.ui.theme.MyIcons
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsCard(
    onToggleMark: () -> Unit,
    onClick: () -> Unit,
    isMarked: Boolean,
    newsItem: NewsItem,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(Dimensions.standardPadding),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column {
            NewsHeaderImage(newsItem.headerImageUrl)
            Box(
                modifier = Modifier.padding(Dimensions.standardPadding)
            ) {
                Column {
                    Spacer(modifier = Modifier.height(Dimensions.dimension12))
                    Row {
                        NewsTitle(
                            newsItem.title,
                            modifier = Modifier.fillMaxWidth(Material.newsTitleWeight)
                        )
                        Spacer(modifier = Modifier.weight(Material.fullWeight))
                        MarkButton(isMarked, onToggleMark)
                    }
                    Spacer(modifier = Modifier.height(Dimensions.dimension12))
                    NewsDescription(newsItem.content)
                    Spacer(modifier = Modifier.height(Dimensions.dimension12))
                    NewsTagSection(topics = newsItem.topicItems)
                }
            }
        }
    }
}

@Composable
fun NewsTitle(
    newsResourceTitle: String,
    modifier: Modifier = Modifier
) {
    Text(newsResourceTitle, style = MaterialTheme.typography.headlineSmall, modifier = modifier)
}

@Composable
fun NewsDescription(
    newsDescription: String
) {
    Text(newsDescription, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun MarkButton(
    isMarked: Boolean,
    onToggleMark: () -> Unit,
) {
    MyIconToggleButton(
        selected = isMarked,
        onCheckedChange = { onToggleMark() },
        topicIcon = MyIcons.Mark
    )
}

@Composable
fun NewsTagSection(
    topics: List<TopicItem>,
    modifier: Modifier = Modifier
) {
    var expandedTopicId by remember { mutableStateOf<String?>(null) }
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.dimension4),
    ) {
        topics.forEach {
            NewsTag(
                expanded = expandedTopicId == it.id,
                text = { Text(text = it.name.uppercase(Locale.getDefault())) },
                onDropdownMenuToggle = { show ->
                    expandedTopicId = if (show) it.id else null
                }
            )
        }
    }
}

@Composable
fun NewsTag(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    enabled: Boolean = true,
    expanded: Boolean = false,
    onDropdownMenuToggle: (show: Boolean) -> Unit = {},
) {
    Box(modifier = modifier) {
        TextButton(
            onClick = { onDropdownMenuToggle(true) },
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primaryContainer)
            )
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.labelSmall) { text() }
            NewsDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onDropdownMenuToggle(false) },
                items = listOf(UNFOLLOW, BROWSE),
                onItemClick = onClickItem(),
                itemText = loadItemText()
            )
        }
    }
}

private fun loadItemText() = @Composable { item: Int ->
    when (item) {
        UNFOLLOW -> {
            Text(stringResource(R.string.unfollow))
        }

        BROWSE -> {
            Text(stringResource(R.string.browse_topic))
        }
    }
}

private fun onClickItem() = { item: Int ->
    when (item) {
        UNFOLLOW -> {}
        BROWSE -> {}
    }
}

@Composable
fun <T> NewsDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    dismissOnItemClick: Boolean = true,
    itemText: @Composable (item: T) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { itemText(item) },
                onClick = {
                    onItemClick(item)
                    if (dismissOnItemClick) onDismissRequest()
                },
            )
        }
    }
}

@Composable
fun NewsHeaderImage(headerImageUrl: String?) {
    AsyncImage(
        placeholder = painterResource(R.drawable.ic_placeholder_default),
        error = painterResource(R.drawable.ic_placeholder_default),
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.dimension180),
        contentScale = ContentScale.Crop,
        model = headerImageUrl,
        contentDescription = null
    )
}

private const val UNFOLLOW = 1
private const val BROWSE = 2