package com.uxstate.diary.presentation.screens.auth

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState
import com.uxstate.diary.presentation.screens.auth.components.AuthenticationContent
import com.uxstate.diary.util.Constants.CLIENT_ID
import timber.log.Timber

@Destination
@RootNavGraph(start = true)


@Composable
fun AuthenticationScreen() {

    //from Stev's Libs
    val oneTapState = rememberOneTapSignInState()
    val messageBarState = rememberMessageBarState()
    Scaffold(content = {

        ContentWithMessageBar(messageBarState =messageBarState ) {
            AuthenticationContent(
                    loadingState =oneTapState.opened,

                    onButtonClicked = { oneTapState.open() })

        }
        }


    )


    OneTapSignInWithGoogle(
            state = oneTapState,
            clientId = CLIENT_ID,
            onTokenIdReceived = { tokenId ->
                messageBarState.addSuccess("Successfully Authenticated!")
                Timber.i(tokenId)
            },
            onDialogDismissed = { message ->
                messageBarState.addError(Exception(message))
                Timber.i(message)
            }
    )
}


