package com.weiran.mynowinandroid.foryou.ui

import android.content.Context
import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.component.MyOverlayLoadingWheel
import com.weiran.mynowinandroid.component.NewsCard
import com.weiran.mynowinandroid.component.TopicSection
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.foryou.FeedAction
import com.weiran.mynowinandroid.foryou.FeedUIState
import com.weiran.mynowinandroid.foryou.ForYouViewModel
import com.weiran.mynowinandroid.foryou.TopicsSectionUiState
import com.weiran.mynowinandroid.theme.Colors.WHITE_GRADIENTS
import com.weiran.mynowinandroid.theme.Dimensions
import com.weiran.mynowinandroid.utils.BrowserUtil.launchCustomBrowserTab

@Composable
fun ForYouScreen() {
    val forYouViewModel: ForYouViewModel = viewModel()
    val feedState = forYouViewModel.forYouState.collectAsState().value
    val dispatchAction = forYouViewModel::dispatchAction
    val context = LocalContext.current

    MyOverlayLoadingWheel(isFeedLoading = feedState.feedUIState is FeedUIState.Loading)
    LazyColumn {
        item {
            when (feedState.topicsSectionUIState) {
                is TopicsSectionUiState.Shown -> ShownContent(
                    feedState.topicItems,
                    dispatchAction,
                    feedState.doneShownState
                )

                is TopicsSectionUiState.NotShown -> Unit
            }
        }
        feedState.newsItems.forEach {
            item(it.id) {
                NewsItem(it, dispatchAction, context)
            }
        }
    }
}

@Composable
private fun NewsItem(
    newsItem: NewsItem,
    dispatchAction: (action: FeedAction) -> Unit,
    context: Context
) {
    val resourceUrl by remember { mutableStateOf(Uri.parse(newsItem.url)) }
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    NewsCard(
        onToggleMark = { dispatchAction(FeedAction.MarkNews(newsItem.id)) },
        onClick = { launchCustomBrowserTab(context, resourceUrl, backgroundColor) },
        isMarked = newsItem.isMarked,
        newsItem = newsItem,
        modifier = Modifier
            .background(Brush.verticalGradient(colors = WHITE_GRADIENTS))
            .padding(Dimensions.standardSpacing)
    )
}

@Composable
private fun ShownContent(
    topicItems: List<TopicItem>,
    dispatchAction: (action: FeedAction) -> Unit,
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
            dispatchAction = dispatchAction,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                enabled = doneButtonState,
                onClick = { dispatchAction.invoke(FeedAction.DoneDispatch) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimensions.buttonPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground
                )
            ) { Text(text = stringResource(R.string.done)) }
        }
    }
}