package com.abhi165.noober.ui.components

import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.abhi165.noober.NoobRepository
import com.abhi165.noober.ui.APICallScreen
import com.abhi165.noober.ui.APITabScreen
import com.abhi165.noober.ui.AvailableSharedPreferencesScreen
import com.abhi165.noober.ui.LogsScreen
import com.abhi165.noober.ui.MoreScreen
import com.abhi165.noober.ui.SearchScreen
import com.abhi165.noober.ui.SharedPrefSettingView
import com.abhi165.noober.ui.SharedPreferenceDataScreen
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
internal fun RootNavGraph(navigator: Navigator, modifier: Modifier) {
    NavHost(
        modifier = modifier,
        navigator = navigator,
        initialRoute = NavigationRoute.BottomNavItem.Home.route,
    ) {
        scene(NavigationRoute.BottomNavItem.Home.route) {
            APICallScreen {
                navigator.navigate(NavigationRoute.APIInfo.route.replace("{index}", it.toString()))
            }
        }

        scene(
            NavigationRoute.BottomNavItem.Properties.route,
            navTransition = NavTransition(
                createTransition = slideInHorizontally(),
            )
        ) {
            AvailableSharedPreferencesScreen {
                navigator.navigate(NavigationRoute.PrefData.route.replace("{prefIdentifier}", it))
            }
        }

        scene(
            NavigationRoute.BottomNavItem.Logs.route,
            navTransition = NavTransition(
                createTransition = slideInHorizontally(),
            )
        ) {
            LogsScreen()
        }

        scene(
            NavigationRoute.BottomNavItem.More.route,
            navTransition = NavTransition(
                createTransition = slideInHorizontally(),
            )
        ) {
            MoreScreen()
        }

        scene(NavigationRoute.APIInfo.route) {
            val index: Int = it.path<Int>("index") ?: -1
            val state = NoobRepository.requestList.value[index]
              APITabScreen(state)
        }

        scene(NavigationRoute.SharedPrefSetting.route) {
              SharedPrefSettingView {
                  navigator.goBack()
              }
        }

        scene(
            NavigationRoute.PrefData.route,
            navTransition = NavTransition(
                createTransition = slideInHorizontally(),
            )
        ) {
            val identifier: String = it.path<String>("prefIdentifier") ?: ""
            SharedPreferenceDataScreen(prefIdentifier = identifier)
        }

        scene(NavigationRoute.Search.route) {
            SearchScreen(searchState = NoobRepository.searchedData, onAPICallTapped = {
                val index = NoobRepository.requestList.value.indexOf(it)
                NoobRepository.changeSearchWidgetState(SearchWidgetState.CLOSED)
                navigator.navigate(NavigationRoute.APIInfo.route.replace("{index}", index.toString()),
                    options = NavOptions(launchSingleTop = true, popUpTo = PopUpTo(NavigationRoute.BottomNavItem.Home.route, inclusive = false)))
            }, goBack = {
                navigator.goBack()
                NoobRepository.changeSearchWidgetState(SearchWidgetState.CLOSED)
            })
        }
    }
}