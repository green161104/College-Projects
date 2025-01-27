package ipp.estg.mobile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ipp.estg.mobile.ui.screens.PlantInformation
import ipp.estg.mobile.ui.screens.StartPage
import ipp.estg.mobile.ui.screens.UserPlantScreen
import ipp.estg.mobile.ui.screens.auth.LoginPage
import ipp.estg.mobile.ui.screens.auth.RegisterPage
import ipp.estg.mobile.ui.screens.diary.DiaryScreen
import ipp.estg.mobile.ui.screens.diary.LogDetailScreen
import ipp.estg.mobile.ui.screens.main.MainScreen
import ipp.estg.mobile.ui.screens.profile.EditPasswordPage
import ipp.estg.mobile.ui.screens.profile.EditProfileImagePage
import ipp.estg.mobile.ui.screens.profile.EditUserPreferencesPage
import ipp.estg.mobile.ui.screens.profile.ProfileScreen
import ipp.estg.mobile.ui.theme.MobileTheme
import ipp.estg.mobile.utils.Screen

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileTheme {
                LeaflingsApp()
            }
        }
    }


    @Composable
    fun LeaflingsApp() {
        val navController = rememberNavController()

        // Set up a NavHost to hold different composable destinations (screens) (rotas)
        NavHost(
            navController = navController,
            startDestination = Screen.Start.route,
        ) {
            composable(Screen.Start.route) {
                StartPage(navController)
            }
            composable(Screen.Login.route) {
                LoginPage(navController)
            }
            composable(Screen.Register.route) {
                RegisterPage(navController)
            }
            composable(Screen.PlantInformation.route + "/{plantId}") {
                val plantId = it.arguments?.getString("plantId")
                val plantIdInt = plantId?.toIntOrNull() ?: 0

                PlantInformation(
                    plantId = plantIdInt,
                    goBack = {
                        navController.popBackStack()
                        navController.navigate(Screen.Main.route)
                    }
                )
            }
            composable(Screen.Main.route) {
                MainScreen(navController = navController)
            }
            composable(Screen.Main.route + "/{tabIndex}") {
                // Igual ao de cima mas pode-se selecionar um tab especifico
                val tabIndex = it.arguments?.getString("tabIndex")
                val defaultTabIndex = tabIndex?.toIntOrNull() ?: 1
                MainScreen(
                    defaultTabIndex = defaultTabIndex,
                    navController = navController
                )
            }
            composable(Screen.ProfileScreen.route) {
                ProfileScreen(navController)
            }
            composable(Screen.EditUserPreferences.route) {
                EditUserPreferencesPage(navController)
            }
            composable(Screen.EditPasswordPage.route) {
                EditPasswordPage(navController)
            }
            composable(Screen.EditProfileImagePage.route) {
                EditProfileImagePage(navController)
            }
            composable(Screen.UserPlant.route + "/{plantId}/{userPlantId}") {
                val plantId = it.arguments?.getString("plantId")
                val plantIdInt = plantId?.toIntOrNull() ?: 0
                val userPlantId = it.arguments?.getString("userPlantId")
                val userPlantIdInt = userPlantId?.toIntOrNull() ?: 0
                UserPlantScreen(
                    plantId = plantIdInt,
                    userPlantId = userPlantIdInt,
                    goBack = {
                        navController.popBackStack()
                        navController.navigate(Screen.Main.route)
                    },
                    goToLogs = { diaryId ->
                        navController.navigate(Screen.UserPlantLogs.route + "/${diaryId}")
                    }
                )
            }
            composable(Screen.UserPlantLogs.route + "/{diaryId}") {
                val diaryId = it.arguments?.getString("diaryId")
                val diaryIdInt = diaryId?.toIntOrNull() ?: 0
                DiaryScreen(
                    diaryId = diaryIdInt,
                    onLogDetailClick = { logId ->
                        navController.navigate(Screen.LogDetails.route + "/${logId}")
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.LogDetails.route + "/{logId}") {
                val logId = it.arguments?.getString("logId")
                val logIdInt = logId?.toIntOrNull() ?: 0
                LogDetailScreen(
                    logId = logIdInt,
                    onLogDeleted = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}
