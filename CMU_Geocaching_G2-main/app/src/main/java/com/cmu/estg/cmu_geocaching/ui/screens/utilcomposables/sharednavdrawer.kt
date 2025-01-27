package com.cmu.estg.cmu_geocaching.ui.screens.utilcomposables

import androidx.compose.foundation.background
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope


@Composable
fun SharedModalNavigationDrawer(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    content: @Composable () -> Unit // Accepts the composable content to wrap
)
    {

        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    Text("Quick Access", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text = "homepage", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("homepage") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "profile", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("profile") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "register", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("register") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "login", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("login") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "settings", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("settings") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "cachehistory", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("cachehistory") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "cacheinformation", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("cacheinformation") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "lookforcache", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("lookforcache") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "newcache", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("newcache") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "triviaanswer", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("triviaanswer") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "triviaquestion", color = MaterialTheme.colorScheme.onSurface) },
                        selected = false,
                        onClick = { navController.navigate("triviaquestion") }
                    )
                }
            },
            drawerState = drawerState,
            gesturesEnabled = true,
            scrimColor = Color.Transparent,
            content = content)
    }


