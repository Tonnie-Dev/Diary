package com.uxstate.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.ramcosta.composedestinations.annotation.Destination
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.uxstate.auth.components.AuthenticationContent
import com.uxstate.util.Constants.CLIENT_ID

//import com.uxstate.diary.presentation.keepSplashScreen

// TODO: Revisit keepSplashScreen

interface AuthScreenNavigator {

    fun navigateToHomeScreen()
}


@Destination
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AuthenticationScreen(
    viewModel: AuthViewModel = hiltViewModel(), navigator: AuthScreenNavigator
) {


    //Observe loading state from viewModel
    val loadingState by viewModel.loadingState
    val isAuthenticated by viewModel.isAuthenticated

    //from Stev's Libs
    val oneTapState = rememberOneTapSignInState()
    val messageBarState = rememberMessageBarState()




    Scaffold(modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
            content = {

                ContentWithMessageBar(messageBarState = messageBarState) {
                    AuthenticationContent(loadingState = loadingState,

                            onButtonClicked = {
                                oneTapState.open()
                                viewModel.setLoading(true)
                            })

                }

            }


    )


    OneTapSignInWithGoogle(state = oneTapState,
            clientId = CLIENT_ID,
            onTokenIdReceived = {

                //token id from Google
                tokenId ->


                val credentials = GoogleAuthProvider.getCredential(tokenId, null)
                FirebaseAuth.getInstance()
                        .signInWithCredential(credentials)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                viewModel.signInWithMongoAtlas(tokenId = tokenId, onSuccess = {

                                    messageBarState.addSuccess("Successfully Authenticated")
                                    viewModel.setLoading(false)

                                }, onError = {

                                    messageBarState.addError(it)
                                    viewModel.setLoading(false)
                                })
                            } else {

                                task.exception?.let { messageBarState.addError(it) }
                                viewModel.setLoading(false)
                            }
                        }
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            })


    LaunchedEffect(key1 = isAuthenticated, block = {

        if (isAuthenticated) {

            navigator.navigateToHomeScreen()
        }
    })

    // TODO: fix keepSplashScreen
    //Launched Effect to control splash screen

    /* LaunchedEffect(key1 = Unit, block = {

         keepSplashScreen = false
     }
     )*/
}


