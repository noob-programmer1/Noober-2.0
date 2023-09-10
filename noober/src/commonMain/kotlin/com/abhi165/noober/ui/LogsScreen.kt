package com.abhi165.noober.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhi165.noober.AccountManager
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.getAccountManager
import com.abhi165.noober.share
import com.abhi165.noober.ui.components.LogRow
import com.abhi165.noober.util.collectAsStateWithLifecycleOrCollectAsState
import kotlinx.coroutines.launch

@Composable
internal fun LogsScreen(
    accountManager: AccountManager = getAccountManager()
) {
    val logs by NoobRepository.logsList.collectAsStateWithLifecycleOrCollectAsState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
    ) {
        item {
            Text(
                text = "Logs",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(logs) {model ->
            LogRow(model) {stackTrace ->
                coroutineScope.launch {
                    val accountUsed = accountManager.generateDeepLink()
                    val message = "Account Used -> $accountUsed \n\n $stackTrace"
                    share(message)
                }
            }
        }
    }
}

