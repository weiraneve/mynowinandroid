package com.weiran.mynowinandroid.modules.saved.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.common.utils.BrowserUtil.launchCustomBrowserTab
import com.weiran.mynowinandroid.modules.saved.SavedAction
import com.weiran.mynowinandroid.modules.saved.SavedUIState
import com.weiran.mynowinandroid.modules.saved.SavedViewModel
import com.weiran.mynowinandroid.store.data.model.NewsItem
import com.weiran.mynowinandroid.ui.component.NewsCard
import com.weiran.mynowinandroid.ui.theme.Colors
import com.weiran.mynowinandroid.ui.theme.Dimensions

@Composable
fun SavedScreen(viewModel: SavedViewModel) {
    val state = viewModel.savedState.collectAsStateWithLifecycle().value
    val action = viewModel::dispatchAction

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.savedUIState) {
            is SavedUIState.Empty -> ShownEmptyContent()
            is SavedUIState.NonEmpty -> Unit
        }
        LazyColumn {
            state.markedNewsItems.forEach {
                item(it.id) {
                    MarkedNewsItem(it, action)
                }
            }
        }
    }

}

@Composable
private fun MarkedNewsItem(
    newsItem: NewsItem,
    savedAction: (action: SavedAction) -> Unit
) {
    val resourceUrl by remember { mutableStateOf(Uri.parse(newsItem.url)) }
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val context = LocalContext.current
    NewsCard(
        onToggleMark = { savedAction(SavedAction.MarkNews(newsItem.id)) },
        onClick = { launchCustomBrowserTab(context, resourceUrl, backgroundColor) },
        isMarked = true,
        newsItem = newsItem,
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = Colors.WHITE_GRADIENTS
                )
            )
            .padding(Dimensions.standardSpacing)
    )
}

@Composable
private fun ShownEmptyContent() {
    Spacer(modifier = Modifier.height(Dimensions.dimension128))
    Image(
        modifier = Modifier.fillMaxWidth(),
        painter = painterResource(id = R.drawable.img_empty_saved),
        contentDescription = null
    )
    Spacer(modifier = Modifier.height(Dimensions.standardPadding))
    Text(
        text = stringResource(id = R.string.saved_empty_message),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}