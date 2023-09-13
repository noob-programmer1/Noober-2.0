package com.abhi165.noober.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhi165.noober.model.state.HeaderBodyState
import com.abhi165.noober.share

@Composable
internal fun RequestResponseInfoScreen(
    modifier: Modifier = Modifier,
    state: HeaderBodyState
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (state.headers.isNotEmpty()) {
            Text(
                text = "Header",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 16.dp)
            )

            for ((key, value) in state.headers) {
                InfoCell(title = key, subtitle = value)
            }
        }

        if (state.body.isNotEmpty()) {
            Text(
                text = "Body",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = { share(state.body) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Sharp.Share,
                    contentDescription = "Share JSON Data"
                )

                Text(
                    text = "Share",
                    fontWeight = FontWeight.SemiBold
                )
            }

            SelectionContainer(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .border(1.dp, Color.Gray, MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
            ) {
                Text(
                    text = state.body,
                    style = MaterialTheme.typography.bodyLarge,
                )

            }
        }

        if (state.headers.isEmpty() && state.body.isEmpty()) {
            Text("Nothing to see here 😏")
        }
    }
}