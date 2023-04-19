package com.uxstate.diary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.Route
import com.uxstate.data.repository.MongoDB
import com.uxstate.diary.presentation.screens.NavGraphs
import com.uxstate.diary.presentation.screens.destinations.AuthenticationScreenDestination
import com.uxstate.diary.presentation.screens.destinations.HomeScreenDestination
import com.uxstate.diary.presentation.ui.theme.DiaryTheme
import com.uxstate.diary.util.Constants.APP_ID
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import timber.log.Timber


@AndroidEntryPoint
//xCVZ62a1SUMhANpf Tonnie
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //from Splash API
        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window,false)


        setContent {
            DiaryTheme {


                DestinationsNavHost(navGraph = NavGraphs.root, startRoute = getStartDestination())

            }
        }
    }


    private fun getStartDestination(): Route {

        //App.create() exists as singleton and can be called severally
        val user = App.create(APP_ID).currentUser

        return if (user != null && user.loggedIn)
            HomeScreenDestination
        else
            AuthenticationScreenDestination
    }
}
