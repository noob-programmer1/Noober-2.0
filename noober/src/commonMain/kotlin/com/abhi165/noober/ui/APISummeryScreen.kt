package com.abhi165.noober.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.abhi165.noober.model.state.APISummaryState


@Composable
internal fun APISummeryScreen(
    state: APISummaryState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {

            if(state.error?.isNotEmpty() == true) {
                InfoCell(
                    title = "Error",
                    subtitle = state.error,
                    subtitleColor = Color.Red
                )
            }

            InfoCell(
                title = "Status Code",
                subtitle = state.statusCode,
                subtitleColor = state.statusCodeColor
            )
            InfoCell(
                title = "Method",
                subtitle = state.method
            )
            InfoCell(
                title = "URL",
                subtitle = state.url
            )
            InfoCell(
                title = "Request Time",
                subtitle = state.requestTime
            )
            InfoCell(
                title = "Response Time",
                subtitle = state.responseTime
            )
            InfoCell(
                title = "Time Taken",
                subtitle = state.timeInterval
            )
            InfoCell(
                title = "Timeout",
                subtitle = state.timeout
            )
            InfoCell(
                title = "cURL",
                subtitle = state.cURL
            )

            InfoCell(
                title = "Cache Policy",
                subtitle = state.cache
            )
        }
    }
}

@Composable
fun InfoCell(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    subtitleColor: Color = Color.Black,
    showDivider: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = subtitle,
            color = subtitleColor,
            style = MaterialTheme.typography.bodyLarge
        )

        if (showDivider) {
            Divider(
                modifier = modifier
                    .padding(top = 2.dp)
            )
        }
    }

}