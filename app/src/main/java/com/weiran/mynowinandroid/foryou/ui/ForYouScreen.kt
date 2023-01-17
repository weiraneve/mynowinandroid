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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.component.MyOverlayLoadingWheel
import com.weiran.mynowinandroid.component.NewsCard
import com.weiran.mynowinandroid.component.TopicSection
import com.weiran.mynowinandroid.data.model.NewsItem
import com.weiran.mynowinandroid.data.model.TopicItem
import com.weiran.mynowinandroid.foryou.FeedUIState
import com.weiran.mynowinandroid.foryou.ForYouAction
import com.weiran.mynowinandroid.foryou.ForYouViewModel
import com.weiran.mynowinandroid.foryou.TopicsSectionUiState
import com.weiran.mynowinandroid.theme.Colors.WHITE_GRADIENTS
import com.weiran.mynowinandroid.theme.Dimensions
import com.weiran.mynowinandroid.utils.BrowserUtil.launchCustomBrowserTab

@Composable
fun ForYouScreen(viewModel: ForYouViewModel = viewModel()) {
    val state = viewModel.forYouState.collectAsStateWithLifecycle().value
    val action = viewModel::dispatchAction
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    viewModel.observeData()
                }

                else -> {}
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    }

    MyOverlayLoadingWheel(isFeedLoading = state.feedUIState is FeedUIState.Loading)
    LazyColumn {
        item {
            when (state.topicsSectionUIState) {
                is TopicsSectionUiState.Shown -> ShownContent(
                    state.topicItems,
                    action,
                    state.doneShownState
                )

                is TopicsSectionUiState.NotShown -> Unit
            }
        }
        state.newsItems.forEach {
            item(it.id) {
                NewsItemCard(
                    newsItem = it,
                    forYouAction = action,
                    context = context
                )
            }
        }
    }
}

@Composable
private fun NewsItemCard(
    newsItem: NewsItem,
    forYouAction: (action: ForYouAction) -> Unit,
    context: Context
) {
    val resourceUrl by remember { mutableStateOf(Uri.parse(newsItem.url)) }
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    NewsCard(
        onToggleMark = { forYouAction(ForYouAction.MarkNews(newsItem.id)) },
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
    forYouAction: (action: ForYouAction) -> Unit,
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
            forYouAction = forYouAction,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                enabled = doneButtonState,
                onClick = { forYouAction.invoke(ForYouAction.DoneDispatch) },
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