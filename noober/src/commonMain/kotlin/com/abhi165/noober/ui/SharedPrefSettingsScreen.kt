package com.abhi165.noober.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.model.AvailablePrefModel

@Composable
internal fun SharedPrefSettingView(onPrefSaved: ()-> Unit) {
    val availablePref = NoobRepository.getAvailablePreferences().toMutableList()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Shared Preferences",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        Divider(thickness = 0.5.dp)

        Text(
            text = "Following shared preferences were found. Pleases check those which are stored using encrypted shared preference.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )
        PrefCheckMarkView(availablePref) {
            NoobRepository.updateNoobPrefValue(availablePref)
            onPrefSaved()
        }
    }

}

@Composable
private fun PrefCheckMarkView(
    availablePref: MutableList<AvailablePrefModel>,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        availablePref.forEachIndexed { index, pref ->
            var checked by remember {
                mutableStateOf(pref.isEncrypted)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked_ ->
                        checked = checked_
                        availablePref[index] = pref.copy(isEncrypted = checked)
                    }
                )

                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = pref.prefIdentifier
                )
            }
        }

        Button(onClick = onClick, modifier = Modifier.align(Alignment.End).padding(end = 16.dp)) {
            Text("Save")
        }
    }
}