package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.ui.component.MyIcons
import com.weiran.mynowinandroid.ui.component.MyTopBar
import com.weiran.mynowinandroid.ui.component.TopicSelection
import com.weiran.mynowinandroid.viewmodel.TopicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForYouScreen(
    viewModel: TopicViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            MyTopBar(
                modifier = Modifier.zIndex(-1F),
                title = R.string.app_title,
                actionIcon = MyIcons.Settings,
            )
        }
    ) { innerPadding ->
        Modifier.padding(innerPadding)

        val dispatchAction = viewModel::dispatchAction
        val topicItems = viewModel.topicState.collectAsState().value.topicItems

        Column(
            modifier = Modifier.padding(top = 80.dp)
        ) {
            Text(
                text = stringResource(R.string.for_you_title),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(R.string.for_you_subtitle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            TopicSelection(
                Modifier.padding(bottom = 8.dp),
                topicItems,
                dispatchAction
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .width(364.dp),
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
}