package ipp.estg.mobile.utils

sealed class Screen(val route: String) {
    data object Start: Screen("start")
    data object Login: Screen("login")
    data object Register: Screen("register")
    data object Main: Screen("main")
    data object PlantInformation: Screen("plantInformation")
    data object EditUserPreferences: Screen("editUserPreferences")
    data object ProfileScreen: Screen("profileScreen")
    data object EditPasswordPage: Screen("editPasswordPage")
    data object EditProfileImagePage: Screen("editProfileImagePage")
    data object UserPlant: Screen("userPlant")
    data object UserPlantLogs: Screen("userPlantLogs")
    data object LogDetails: Screen("logDetails")
}