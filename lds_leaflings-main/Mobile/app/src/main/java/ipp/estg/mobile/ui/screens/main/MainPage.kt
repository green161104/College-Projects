package ipp.estg.mobile.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ipp.estg.mobile.ui.components.navigation.BottomNavigationBar
import ipp.estg.mobile.ui.components.navigation.NavigationItem
import ipp.estg.mobile.ui.screens.main.tabs.MainTab
import ipp.estg.mobile.ui.screens.main.tabs.MatchingPlantsTab
import ipp.estg.mobile.ui.screens.main.tabs.ProfileTab
import ipp.estg.mobile.utils.Screen
import ipp.estg.mobile.viewModels.AdViewModel
import ipp.estg.mobile.viewModels.PlantViewModel
import ipp.estg.mobile.viewModels.UserPlantViewModel
import ipp.estg.mobile.viewModels.UserViewModel

val items = listOf(
    NavigationItem(
        title = "Profile",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle,
    ),
    NavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    NavigationItem(
        title = "Matching",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    )
)

@Composable
fun MainScreen(
    defaultTabIndex: Int = 1,
    navController: NavController
) {

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(defaultTabIndex) }


    val userPlantViewModel: UserPlantViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val plantViewModel: PlantViewModel = viewModel()
    val adViewModel: AdViewModel = viewModel()

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
        bottomBar = {
            BottomNavigationBar(
                items = items,
                selectedItemIndex = selectedItemIndex,
                onItemSelected = { selectedIndex ->
                    selectedItemIndex = selectedIndex
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    ) { innerPadding ->
        // Apply padding from the Scaffold to avoid content being hidden under top/bottom bars
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding),
        ) {
            when (selectedItemIndex) {
                0 -> ProfileTab(userPlantViewModel, userViewModel, navController)
                1 -> MainTab(navController, plantViewModel, adViewModel)
                2 -> MatchingPlantsTab(
                    userViewModel = userViewModel,
                    goToPlantInfo = { plantId -> navController.navigate(Screen.PlantInformation.route + "/$plantId") }
                )
            }
        }
    }

}