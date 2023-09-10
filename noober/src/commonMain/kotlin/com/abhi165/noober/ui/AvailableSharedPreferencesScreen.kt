package com.abhi165.noober.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.isAndroid
import com.abhi165.noober.ui.components.AvailablePrefRow

@Composable
internal fun AvailableSharedPreferencesScreen(
    modifier: Modifier = Modifier,
    onPrefClicked: (String) -> Unit
) {

    if (isAndroid()) {
        var showPrefSetting by remember { mutableStateOf(!NoobRepository.hasPrefEncryptionData()) }

        if (showPrefSetting) {
            SharedPrefSettingView {
                showPrefSetting = !NoobRepository.hasPrefEncryptionData()
            }
        } else {
            val availablePrefList by remember { mutableStateOf(NoobRepository.getAvailablePreferences()) }

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp)
            ) {
                items(availablePrefList) {
                    AvailablePrefRow(identifier = it.prefIdentifier) {
                        onPrefClicked(it.prefIdentifier)
                    }
                }
            }

        }
    } else {
        SharedPreferenceDataScreen()
    }
}