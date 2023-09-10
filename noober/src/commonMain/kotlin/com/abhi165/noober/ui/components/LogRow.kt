package com.abhi165.noober.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
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
import com.abhi165.noober.model.LogModel


@Composable
internal fun LogRow(
    model: LogModel,
    onShareClicked: (String) -> Unit
) {
    SelectionContainer(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(
                    if(model.isError) Color.Red.copy(0.1f) else  Color.LightGray.copy(0.1f),
                    MaterialTheme.shapes.medium
                )
                .border(1.dp, Color.DarkGray.copy(0.6f), MaterialTheme.shapes.medium )
                .padding(8.dp)
        ) {
            if(model.isError) {
                Button(
                    onClick = { onShareClicked(model.value) },
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
            }

            Text(
                text = model.date,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = model.tag,
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(text = model.value)
        }
    }
}