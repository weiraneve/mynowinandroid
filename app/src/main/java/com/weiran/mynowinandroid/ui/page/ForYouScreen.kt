package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.ui.component.NewsCard
import com.weiran.mynowinandroid.ui.component.TopicSection
import com.weiran.mynowinandroid.ui.theme.Colors.WHITE_GRADIENTS
import com.weiran.mynowinandroid.ui.theme.Dimensions
import com.weiran.mynowinandroid.viewmodel.NewsAction
import com.weiran.mynowinandroid.viewmodel.NewsViewModel
import com.weiran.mynowinandroid.viewmodel.SectionUiState
import com.weiran.mynowinandroid.viewmodel.TopicAction
import com.weiran.mynowinandroid.viewmodel.TopicViewModel

@Composable
fun ForYouScreen() {
    val topicViewModel: TopicViewModel = viewModel()
    val topicState = topicViewModel.topicState.collectAsState().value
    val newsViewModel: NewsViewModel = viewModel()
    val newsState = newsViewModel.newsState.collectAsState().value

    LazyColumn {
        item {
            when (topicState.sectionUiState) {
                is SectionUiState.Shown -> ShownContent(
                    topicState.topicItems,
                    topicViewModel::dispatchAction,
                    newsViewModel::dispatchAction,
                    topicState.doneButtonState
                )

                is SectionUiState.NotShown -> Unit
            }
            newsState.newsItems.forEach {
                NewsCard(
                    onToggleMark = {},
                    onClick = {},
                    isMarked = false, // todo
                    newsItem = it,
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = WHITE_GRADIENTS
                            )
                        )
                        .padding(Dimensions.standardSpacing)
                )
            }
        }
    }

}

@Composable
private fun ShownContent(
    topicItems: List<TopicItem>,
    topicDispatchAction: (action: TopicAction) -> Unit,
    newsDispatchAction: (action: NewsAction) -> Unit,
    doneButtonState: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(Dimensions.standardSpacing))
        Text(
            text = stringResource(R.string.for_you_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(Dimensions.standardSpacing))
        Text(
            text = stringResource(R.string.for_you_subtitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.standardPadding),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        TopicSection(
            topicItems = topicItems,
            topicDispatchAction = topicDispatchAction,
            newsDispatchAction = newsDispatchAction
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                enabled = doneButtonState,
                onClick = { topicDispatchAction.invoke(TopicAction.DoneDispatch) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.buttonPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                ),
            ) {
                Text(
                    text = stringResource(R.string.done)
                )
            }
        }

    }
}