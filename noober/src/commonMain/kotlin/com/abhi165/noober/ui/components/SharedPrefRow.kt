package com.abhi165.noober.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun SharedPrefRow(
    modifier: Modifier = Modifier,
    key: String,
    value: String,
    onValueChanges: (String)-> Unit
    ) {
    var isSelected by remember {
        mutableStateOf(false)
    }

    val bgColour by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Blue.copy(0.01f))

    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable {
                isSelected = !isSelected
            }
            .background(
                bgColour,
                RoundedCornerShape(8.dp)
            )
            .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = Color.DarkGray)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {

        Text(
            text = key,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        AnimatedVisibility(visible = isSelected) {
            InputDialogView(key) {
                it?.let {
                   onValueChanges(it)
                }
                isSelected = false
            }
        }

        AnimatedVisibility(visible = !isSelected) {
            SelectionContainer {
                Text(
                    text = value,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}