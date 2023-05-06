package com.uxstate.diary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.Route
import com.uxstate.diary.data.local.database.ImagesDatabase
import com.uxstate.diary.presentation.screens.NavGraphs
import com.uxstate.diary.presentation.screens.destinations.AuthenticationScreenDestination
import com.uxstate.diary.presentation.screens.destinations.HomeScreenDestination
import com.uxstate.diary.presentation.ui.theme.DiaryTheme
import com.uxstate.diary.util.Constants.APP_ID
import com.uxstate.diary.util.retryUploadingImageToFirebase
import dagger.hilt.android.AndroidEntryPoint
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

var keepSplashScreen = true

@AndroidEntryPoint
//xCVZ62a1SUMhANpf Tonnie
class MainActivity : ComponentActivity() {

    lateinit var 

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
            DiaryTheme {
                DestinationsNavHost(navGraph = NavGraphs.root, startRoute = getStartDestination())

            }
        }


        cleanUpCheck(scope = lifecycleScope, database = )
    }


    private fun getStartDestination(): Route {

        //App.create() exists as singleton and can be called severally
        val user = App.create(APP_ID).currentUser

        return if (user != null && user.loggedIn)
            HomeScreenDestination
        else
            AuthenticationScreenDestination
    }

    private fun cleanUpCheck(scope: CoroutineScope, database: ImagesDatabase) {
        scope.launch(IO) {

            val unUploadedImages = database.imageToUploadDao.getAllImages()

            unUploadedImages.forEach {

                image ->

                retryUploadingImageToFirebase(
                        imageToUpload = image,
                        onSuccess = {
                            scope.launch(IO) {
                                database.imageToUploadDao.cleanupImage(imageId =image.id)
                            }
                        })
            }
        }
    }
}
