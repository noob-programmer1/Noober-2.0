package com.abhi165.noober.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector


internal sealed class NavigationRoute(val route: String) {
    sealed class BottomNavItem(route: String, val icon: ImageVector, val title: String) :
        NavigationRoute(route) {
        object Home: BottomNavItem("api", Icons.Rounded.Home, "API")
        object Properties : BottomNavItem("properties", Icons.Rounded.List, "Properties")
        object More : BottomNavItem("more", Icons.Rounded.Info, "More")
        object Logs : BottomNavItem("log", Icons.Rounded.ExitToApp, "Logs")
    }

    object APIInfo : NavigationRoute("/info/{index}")
    object SharedPrefSetting : NavigationRoute("pref_setting")
    object Search : NavigationRoute("search")
    object PrefData : NavigationRoute("/pref_data/{prefIdentifier}")
}

@Composable
internal fun BottomBar(
    items: List<NavigationRoute.BottomNavItem>,
    selectedRoute: String,
    onclick: (String) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            addNavItem(
                item = item,
                onClick = { onclick(item.route) },
                isSelected = item.route == selectedRoute
            )
        }
    }
}

@Composable
internal fun RowScope.addNavItem(
    item: NavigationRoute.BottomNavItem,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(imageVector = item.icon, contentDescription = null)
        },
        alwaysShowLabel = true,
        label = {
            Text(text = item.title)
        },
        colors = NavigationBarItemDefaults.colors()
    )
}
