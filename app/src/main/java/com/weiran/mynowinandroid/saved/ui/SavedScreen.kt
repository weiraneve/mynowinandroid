package com.weiran.mynowinandroid.saved.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.component.NewsCard
import com.weiran.mynowinandroid.foryou.FeedAction
import com.weiran.mynowinandroid.foryou.FeedViewModel
import com.weiran.mynowinandroid.foryou.SavedUIState
import com.weiran.mynowinandroid.theme.Colors
import com.weiran.mynowinandroid.theme.Dimensions

@Composable
fun SavedScreen() {
    val feedViewModel: FeedViewModel = viewModel()
    val feedState = feedViewModel.feedState.collectAsState().value
    val dispatchAction = feedViewModel::dispatchAction

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (feedState.savedUIState) {
            is SavedUIState.Empty -> ShownEmptyContent()
            is SavedUIState.NonEmpty -> Unit
        }
        LazyColumn {
            feedState.markedNewsItems.forEach {
                item {
                    NewsCard(
                        onToggleMark = { dispatchAction(FeedAction.MarkNews(it.id)) },
                        onClick = {},
                        isMarked = true,
                        newsItem = it,
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = Colors.WHITE_GRADIENTS
                                )
                            )
                            .padding(Dimensions.standardSpacing)
                    )
                }
            }
        }
    }

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