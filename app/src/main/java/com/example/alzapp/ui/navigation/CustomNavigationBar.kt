package com.example.alzapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun CustomNavigationBar(
    items: List<NavBarItem<out Any>>,
    currentDestination: NavDestination?,
    onItemClick: (NavBarItem<out Any>) -> Unit
) {
    NavigationBar (
        modifier = Modifier.shadow(
            elevation = 10.dp,
            clip = false
        ),
        containerColor = MaterialTheme.colorScheme.surface
    ){
        items.forEach { navItem ->
            val isSelected = currentDestination?.hasRoute(navItem.route::class) == true
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(navItem) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                icon = {
                    Box(modifier = Modifier.size(35.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.label,
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(navItem.iconScale)
                        )
                    }
                },
                label = {
                    Text(
                        text = navItem.label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            )
        }
    }
}
