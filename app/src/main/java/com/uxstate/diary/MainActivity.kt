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




    private fun cleanUpCheck(scope: CoroutineScope, database: ImagesDatabase) {
        scope.launch(IO) {

            val unUploadedImages = database.imageToUploadDao.getAllImages()

            unUploadedImages.forEach {

                image ->

                retryUploadingImageToFirebase(
                        imageToUpload = image,

                        //onSuccess to trigger removal of images from database
                        onSuccess = {
                            scope.launch(IO) {
                                database.imageToUploadDao.cleanupImage(imageId = image.id)
                            }
                        })
            }

            val unDeletedItems = database.imageToDeleteDao.getAllImages()
            unDeletedItems .forEach {

                        imageToDelete ->

                        //call retry delete util function

                        retryDeletingImageToFirebase(imageToDelete = imageToDelete, onSuccess = {
                            scope.launch(IO) {
                                //when we successfully remove images from FB, we cleanup database
                                database.imageToDeleteDao.cleanUpImage(imageToDelete.id)
                            }

                        })
                    }
        }
    }
}

fun retryUploadingImageToFirebase(imageToUpload: ImageToUpload, onSuccess: () -> Unit) {

    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath)
            .putFile(
                    imageToUpload.imageUrl.toUri(),
                    storageMetadata { },
                    imageToUpload.sessionUrl.toUri()
            )

            //add onSuccess Listener instead of OnProgressListener
            .addOnSuccessListener { onSuccess() }
}

fun retryDeletingImageToFirebase(imageToDelete: ImageToDelete, onSuccess: () -> Unit) {

    val storage = FirebaseStorage.getInstance().reference

    storage.child(imageToDelete.remotePath)
            .delete()
            //add onSuccess Listener instead of OnProgressListener
            .addOnSuccessListener { onSuccess() }
}
