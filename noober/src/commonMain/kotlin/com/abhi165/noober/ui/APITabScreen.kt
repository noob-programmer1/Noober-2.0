package com.abhi165.noober.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.abhi165.noober.model.state.APIInfoState

@Composable
internal fun APITabScreen(
    state: APIInfoState
) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Summary", "Request", "Response")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> APISummeryScreen(state = state.summary)
            1 -> RequestResponseInfoScreen(state = state.requestState)
            2 -> RequestResponseInfoScreen(state = state.responseState)
        }
    }
}