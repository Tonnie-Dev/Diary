package com.uxstate.diary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.spec.Route
import com.uxstate.auth.destinations.AuthenticationScreenDestination
import com.uxstate.diary.navigation.NavGraphs
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

var keepSplashScreen = true

@AndroidEntryPoint
//xCVZ62a1SUMhANpf Tonnie
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: ImagesDatabase

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

        //from activity Lifecycle Owner
        cleanUpCheck(scope = lifecycleScope, database = database)
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

                        //onSuccess to trigger removal of images from database
                        onSuccess = {
                            scope.launch(IO) {
                                database.imageToUploadDao.cleanupImage(imageId = image.id)
                            }
                        })
            }

            val unDeletedItems = database.imageToDeleteDao.getAllImages()
                    .forEach {

                        imageToDelete ->

                        //call retry delete util function

                        retryDeletingImageToFirebase(imageToDelete = imageToDelete, onSuccess = {
                            scope.launch (IO){
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
