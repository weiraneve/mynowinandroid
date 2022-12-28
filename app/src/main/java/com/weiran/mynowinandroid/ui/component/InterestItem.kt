package com.weiran.mynowinandroid.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InterestItem(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() }
                .padding(vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            InterestContent(name)
        }
        MyIconToggleButton(selected = selected)
    }
}

@Composable
private fun InterestContent(
    name: String, modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}