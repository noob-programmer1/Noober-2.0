package com.abhi165.noober.ui

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.material.icons.sharp.Share
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhi165.noober.AccountManager
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.getAccountManager
import com.abhi165.noober.share
import kotlinx.coroutines.launch

@Composable
internal fun MoreScreen(
    modifier: Modifier = Modifier,
    accountManager: AccountManager = getAccountManager()
) {
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    var replaceableText by remember {
        mutableStateOf(NoobRepository.OLD_URL)
    }

    var replacedText by remember {
        mutableStateOf(NoobRepository.NEW_URL)
    }

    CompositionLocalProvider(
        LocalIndication provides rememberRipple(color = Color.Transparent)
    ) {
        Column(
            modifier = modifier
                .clickable {
                    focusManager.clearFocus(true)
                }
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Change URL",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            OutlinedTextField(
                value = replaceableText,
                label = { Text(text = "Current URL") },
                onValueChange = {
                    replaceableText = it
                },
                isError = replaceableText.isNotBlank()
            )
            OutlinedTextField(
                value = replacedText,
                label = { Text(text = "Enter new URL") },
                onValueChange = {
                    replacedText = it
                },
                isError = replacedText.isNotBlank()
            )

            Button(
                onClick = {
                    NoobRepository.changeURL(newURL = replacedText, oldURL = replaceableText)
                    focusManager.clearFocus(true)
                },
                enabled = replacedText.isNotBlank() && replaceableText.isNotBlank()
            ) {
                Text(text = "Change")
            }
            Spacer(modifier = modifier.height(8.dp))
            Divider()
            Spacer(modifier = modifier.height(16.dp))

            Text(
                text = "Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        val link = accountManager.generateDeepLink()
                        share(link)
                    }
                }) {
                    Icon(imageVector = Icons.Sharp.Share, contentDescription = "")
                    Text(text = "Share")
                }

                Button(onClick = { accountManager.restoreAccount() }) {
                    Icon(imageVector = Icons.Sharp.Refresh, contentDescription = "")
                    Text(text = "Restore")
                }
            }
            Spacer(modifier = modifier.height(8.dp))
            Divider()
            Spacer(modifier = modifier.height(16.dp))

        }
    }
}