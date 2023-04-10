package com.uxstate.diary.presentation.screens.auth_screen

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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.uxstate.diary.presentation.screens.auth_screen.components.AuthenticationContent
import com.uxstate.diary.presentation.screens.destinations.HomeScreenDestination
import com.uxstate.diary.util.Constants.CLIENT_ID

@Destination
@RootNavGraph(start = true)


@Composable
fun AuthenticationScreen(
    viewModel: AuthViewModel = hiltViewModel(), navigator: DestinationsNavigator
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
            .navigationBarsPadding(), content = {

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
            onTokenIdReceived = { tokenId ->


                viewModel.signInWithMongoAtlas(tokenId = tokenId, onSuccess = {

                    messageBarState.addSuccess("Successfully Authenticated")
                    viewModel.setLoading(false)

                }, onError = {

                    messageBarState.addError(it)
                    viewModel.setLoading(false)
                })
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            })


    LaunchedEffect(key1 = isAuthenticated, block = {

        if (isAuthenticated) {

            navigator.navigate(HomeScreenDestination)
        }
    })
}


