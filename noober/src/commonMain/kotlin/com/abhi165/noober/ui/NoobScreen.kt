package com.abhi165.noober.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.isAndroid
import com.abhi165.noober.ui.components.BottomBar
import com.abhi165.noober.ui.components.NavigationRoute
import com.abhi165.noober.ui.components.NoobAppBar
import com.abhi165.noober.ui.components.RootNavGraph
import com.abhi165.noober.ui.components.SearchWidgetState
import com.abhi165.noober.util.collectAsStateWithLifecycleOrCollectAsState
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.rememberNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoobScreen(
    route: String? = null,
    topSafeArea: Float = 0f
) {
    val items = listOf(
        NavigationRoute.BottomNavItem.Home,
        NavigationRoute.BottomNavItem.Properties,
        NavigationRoute.BottomNavItem.Logs,
        NavigationRoute.BottomNavItem.More
    )
    val navigator = rememberNavigator()
    val navBackStackEntry by navigator.currentEntry.collectAsState(null)
    val currentEntry = navBackStackEntry?.route?.route

    val searchWidgetState by NoobRepository.searchState.collectAsStateWithLifecycleOrCollectAsState()
    val searchState by NoobRepository.searchQuery.collectAsStateWithLifecycleOrCollectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topSafeArea.dp),
        topBar = {
            NoobAppBar(
                searchWidgetState = searchWidgetState,
                canGoBack =  !items.any { it.route == currentEntry },
                showPrefSetting = (currentEntry == NavigationRoute.BottomNavItem.Properties.route) && isAndroid() && NoobRepository.hasPrefEncryptionData(),
                searchTextState =searchState,
                onSettingClicked = {
                    navigator.navigate(NavigationRoute.SharedPrefSetting.route)
                },
                onBackClicked = {
                    NoobRepository.changeSearchWidgetState(SearchWidgetState.CLOSED)
                    navigator.goBack()
                },
                onTextChange = NoobRepository::onSearchQueryCHanged,
                onSearchClicked = {},
                onSearchTriggered = {
                    NoobRepository.changeSearchWidgetState(SearchWidgetState.OPENED)
                    navigator.navigate(NavigationRoute.Search.route, options = NavOptions(launchSingleTop = true))
                }
            )
        },
        bottomBar = {
            if (items.any { it.route == currentEntry })
                BottomBar(
                    items,
                    selectedRoute = currentEntry ?: NavigationRoute.BottomNavItem.Home.route) {selectedRoute ->
                    navigator.navigate(
                        selectedRoute,
                        options = NavOptions(
                            launchSingleTop = true,
                            popUpTo = PopUpTo(
                                NavigationRoute.BottomNavItem.Home.route,
                                false
                            )
                        )
                    )
                }
        }
    ) {
        RootNavGraph(navigator, modifier = Modifier.padding(it))
    }
    LaunchedEffect(route) {
        route?.let {
            navigator.navigate(it, NavOptions(launchSingleTop = true))
        }
    }
}