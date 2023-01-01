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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.ui.theme.MyIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsCard(
    onToggleMark: () -> Unit,
    onClick: () -> Unit,
    isMarked: Boolean,
    news: NewsItem
) {

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        NewsTitle(
                            news.title,
                            modifier = Modifier.fillMaxWidth((.8f))
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        MarkButton(isMarked, onToggleMark)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsDescription(news.content)
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsTopics(topics = news.topics)
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
    onClick: () -> Unit,
) {
    MyIconToggleButton(
        selected = isMarked,
        onCheckedChange = { onClick() },
        topicIcon = MyIcons.Mark //todo
    )
}

@Composable
fun NewsTopics(
    topics: List<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        topics.forEach {
            Text(text = "topic Id: $it", color = Color.Blue)
        } // todo
    }
}