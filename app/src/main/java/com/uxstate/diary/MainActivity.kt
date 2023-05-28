package com.uxstate.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.ramcosta.composedestinations.spec.Route
import com.uxstate.auth.destinations.AuthenticationScreenDestination
import com.uxstate.diary.navigation.AppNavigation
import com.uxstate.diary.utils_firebase.cleanUpCheck
import com.uxstate.home.destinations.HomeScreenDestination
import com.uxstate.mongo.local.database.ImagesDatabase
import com.uxstate.mongo.local.entities.ImageToDelete
import com.uxstate.mongo.local.entities.ImageToUpload
import com.uxstate.ui.theme.DiaryTheme
import com.uxstate.util.Constants.APP_ID
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

var keepSplashScreen = false

@AndroidEntryPoint
//xCVZ62a1SUMhANpf Tonnie
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: ImagesDatabase

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //from Splash API
        installSplashScreen().setKeepOnScreenCondition {

            keepSplashScreen

        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        //initialize firebase
        FirebaseApp.initializeApp(this)
        setContent {

            val navController = rememberAnimatedNavController()

            DiaryTheme {
                //DestinationsNavHost(navGraph = NavGraphs.root, startRoute = getStartDestination())
                AppNavigation(
                        navController = navController,
                        modifier = Modifier
                                .fillMaxSize()
                )
            }
        }

        //from activity Lifecycle Owner
        cleanUpCheck(scope = lifecycleScope, database = database)
    }
}


