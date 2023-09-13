package com.abhi165.noober.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.model.SharedPrefModel
import com.abhi165.noober.ui.components.SharedPrefRow
import com.abhi165.noober.util.collectAsStateWithLifecycleOrCollectAsState


@Composable
internal fun SharedPreferenceDataScreen(
    prefIdentifier: String = "",
    modifier: Modifier = Modifier
) {
    val sharedPrefState by NoobRepository.getPrefData(prefIdentifier)
        .collectAsStateWithLifecycleOrCollectAsState(SharedPrefModel())

    DisposableEffect(Unit) {
        onDispose {
            NoobRepository.cleanResourcesIfNeeded()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 4.dp)
    ) {
        item {
            Text(
                text = prefIdentifier,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }

        items(sharedPrefState.data.toList()) {
            SharedPrefRow(
                key = it.first,
                value = it.second.toString()
            ) { newValue ->
                NoobRepository.addPrefData(
                    key = it.first,
                    newValue = newValue,
                    prefIdentifier = prefIdentifier,
                    oldValue = it.second.toString()
                )
            }
        }
    }
}