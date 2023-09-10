package com.abhi165.noober.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abhi165.noober.model.state.APISummaryState

@Composable
internal fun APIInfoRow(
    modifier: Modifier = Modifier,
    state: APISummaryState,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(0.2f)),
        elevation = CardDefaults.cardElevation(),
        border = BorderStroke(1.dp, state.statusCodeColor.copy(0.2f))
    ) {
        Row(
            modifier = modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            Column(modifier = modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .background(
                    state.statusCodeColor.copy(0.1f),
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
            ) {
                Text(
                    text = state.statusCode,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = state.statusCodeColor
                )
                Text(
                    text = state.method,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleSmall,
                    color =  state.statusCodeColor.copy(0.7f)
                )
            }
            Column {
                Text(
                    text = state.url,
                    maxLines = 2,
                    style = MaterialTheme.typography.labelLarge
                )

                Divider(
                    thickness = 0.5.dp,
                    modifier = modifier.padding(end = 8.dp)
                )

                Text(
                    text = state.requestTime,
                    maxLines = 2,
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp, top = 12.dp)
                )
            }
        }
    }
}