package com.cmu.estg.cmu_geocaching

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cmu.estg.cmu_geocaching.data.local.GeocachingDatabase
import com.cmu.estg.cmu_geocaching.data.local.repository.CacheRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserCacheCrossRefRepository
import com.cmu.estg.cmu_geocaching.data.local.repository.UserRepository
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.CacheHistoryScreen
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.CacheInformationScreen
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.FindCacheScreen
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.GeocachingScreen
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.NewCacheScreen
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.QuestionAnswerScreen
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.RateYourExperienceScreen
import com.cmu.estg.cmu_geocaching.ui.screens.cacheActivity.TriviaQuestionScreen
import com.cmu.estg.cmu_geocaching.ui.screens.homepage.Homepage
import com.cmu.estg.cmu_geocaching.ui.screens.profileScreen.UserProfileScreen
import com.cmu.estg.cmu_geocaching.ui.screens.registerscreen.LoginScreen
import com.cmu.estg.cmu_geocaching.ui.screens.registerscreen.RegistrationScreen
import com.cmu.estg.cmu_geocaching.ui.screens.settings.SettingsScreen
import com.cmu.estg.cmu_geocaching.util.location.LocationRepository
import com.cmu.estg.cmu_geocaching.viewModel.LightSensorViewModel
import com.cmu.estg.cmu_geocaching.viewModel.SettingsViewModel
import com.cmu.estg.cmu_geocaching.viewModel.SharedCacheViewModel
import com.cmu.estg.cmu_geocaching.viewModel.TriviaQuestionViewModel
import com.cmu.estg.cmu_geocaching.viewModel.TriviaQuestionViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Nav(locationRepository: LocationRepository, settingsViewModel: SettingsViewModel){

    val navController = rememberNavController()

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val database = GeocachingDatabase.getDatabase(context)
    val cacheDao = database.cacheDao()
    val userCacheDao = database.userCacheCrossRefDao()
    val userCacheRepository = UserCacheCrossRefRepository(userCacheDao)
    val userDao = database.userDao()
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userRepository = UserRepository(firebaseAuth, firestore, userDao)
    val cacheRepository = CacheRepository(cacheDao, firestore)
    val cacheViewModelFactory =
        SharedCacheViewModel.SharedCacheViewModelFactory(application, cacheRepository, database)
    val sharedCacheViewModel: SharedCacheViewModel = viewModel(factory = cacheViewModelFactory)
    val triviaQuestionViewModelFactory = TriviaQuestionViewModelFactory(application, userCacheRepository, userRepository, cacheRepository)
    val sharedTriviaQuestionViewModel: TriviaQuestionViewModel = viewModel(factory = triviaQuestionViewModelFactory)

    val lightSensorViewModel = remember { LightSensorViewModel(context) }


    NavHost( navController = navController, startDestination = "login"){


        composable(route ="homepage"){
            Homepage(navController, locationRepository, settingsViewModel, lightSensorViewModel)
        }
        composable(route = "register"){
            RegistrationScreen(navController)
        }
        composable(route = "login"){
            LoginScreen(navController)
        }
        composable(route = "profile"){
            UserProfileScreen(navController)
        }
        composable(route = "settings"){
            SettingsScreen(navController, settingsViewModel, lightSensorViewModel)
        }
        composable(route = "cachehistory"){
            CacheHistoryScreen(navController)
        }
        composable(route = "cacheinformation"){
            CacheInformationScreen(navController, sharedCacheViewModel) //cache here second
        }
        composable(route = "findcache"){
            FindCacheScreen(navController, sharedCacheViewModel, locationRepository) //cache here first
        }
        composable(route = "lookforcache"){
            GeocachingScreen(navController, application ,sharedCacheViewModel, locationRepository) //cache needs to go here third
        }
        composable(route = "newcache"){
            NewCacheScreen( navController)
        }
        composable(route = "triviaanswer"){
            QuestionAnswerScreen(sharedTriviaQuestionViewModel, navController, sharedCacheViewModel) //fourth
        }
        composable(route = "triviaquestion"){
            TriviaQuestionScreen(sharedTriviaQuestionViewModel, navController)
        }
        composable(route = "ratecache"){
            RateYourExperienceScreen(navController,sharedCacheViewModel)
        }
    }
}

