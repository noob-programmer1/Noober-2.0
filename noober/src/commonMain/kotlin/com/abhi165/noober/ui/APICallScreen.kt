package com.abhi165.noober.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.ui.components.APIInfoRow
import com.abhi165.noober.util.collectAsStateWithLifecycleOrCollectAsState

@Composable
internal fun APICallScreen(
    onClick: (Int) -> Unit
) {
    val apiCallList by NoobRepository.requestList.collectAsStateWithLifecycleOrCollectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp)
    ) {
        itemsIndexed(apiCallList) { index, state ->
            APIInfoRow(
                state = state.summary,
                onClick = {
                    onClick(index)
                }
            )
        }
    }
}