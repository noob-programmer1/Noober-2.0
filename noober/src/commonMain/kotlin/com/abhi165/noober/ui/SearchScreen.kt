package com.abhi165.noober.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.abhi165.noober.AccountManager
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.getAccountManager
import com.abhi165.noober.model.state.APIInfoState
import com.abhi165.noober.model.state.SearchState
import com.abhi165.noober.share
import com.abhi165.noober.ui.components.APIInfoRow
import com.abhi165.noober.ui.components.LogRow
import com.abhi165.noober.ui.components.SharedPrefRow
import com.abhi165.noober.util.collectAsStateWithLifecycleOrCollectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
internal fun SearchScreen(
    accountManager: AccountManager = getAccountManager(),
    searchState: Flow<SearchState>,
    onAPICallTapped: (APIInfoState) -> Unit,
    goBack: ()-> Unit
) {
   val state by searchState.collectAsStateWithLifecycleOrCollectAsState(SearchState())
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                focusManager.clearFocus(true)
            }
        ,
        contentPadding = PaddingValues(16.dp)
    ) {

        items(state.apiCalls) {
            APIInfoRow(state = it.summary) {
                onAPICallTapped(it)
            }
        }

        state.prefData.forEach {
            items(it.data.toList()) {pref ->
                SharedPrefRow(key = pref.first, value = pref.second.toString()) {newValue ->
                    NoobRepository.addPrefData(
                        key = pref.first,
                        newValue = newValue,
                        prefIdentifier = it.prefName,
                        oldValue = pref.second
                    )
                    goBack()
                }
            }
        }

        items(state.logs) {
            LogRow(it) {stackTrace ->
                coroutineScope.launch {
                    val accountUsed = accountManager.generateDeepLink()
                    val message = "Account Used -> $accountUsed \n\n $stackTrace"
                    share(message)
                }
            }
        }

    }
}