package com.uxstate.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.FirebaseApp
import com.uxstate.auth.isAuthScreenInvoked
import com.uxstate.diary.navigation.AppNavigation
import com.uxstate.diary.utils_firebase.cleanUpCheck
import com.uxstate.home.keepSplashScreen
import com.uxstate.mongo.local.database.ImagesDatabase
import com.uxstate.ui.theme.DiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
//xCVZ62a1SUMhANpf Tonnie
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: ImagesDatabase

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //from Splash API - Creates a SplashScreen instance associated with this Activity
        installSplashScreen().setKeepOnScreenCondition {

            /*
             - keepSplashScreen is defined on the Home Module
             - isAuthScreenInvoked is defined on the Auth Module
             - keepSplashScreen sets the condition to keep the splash screen visible
              - if true the splash screen stays visible until keepSplashScreen is changed to false
            */
            keepSplashScreen && !isAuthScreenInvoked

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


