package com.weiran.mynowinandroid.ui.page

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.weiran.mynowinandroid.R
import com.weiran.mynowinandroid.ui.component.MyTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InterestScreen() {

    Scaffold(
        topBar = {
            MyTopBar(
                modifier = Modifier.zIndex(-1F),
                title = R.string.interests_title,
            )
        }
    ) { innerPadding ->
        Modifier.padding(innerPadding)
    }

}