package com.abhi165.noober.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
internal fun InputDialogView(
    heading: String,
    onDismiss:(String?) -> Unit) {

    var newPrefValue by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = { onDismiss(null) }) {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(8.dp)
            ,
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = heading,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = newPrefValue,
                    onValueChange = { newPrefValue = it },
                    modifier = Modifier.padding(8.dp),
                    label = { Text("Enter new value") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onDismiss(newPrefValue)
                        }
                    )
                )

                Row {
                    OutlinedButton(
                        onClick = { onDismiss(null) },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            onDismiss(newPrefValue) },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}