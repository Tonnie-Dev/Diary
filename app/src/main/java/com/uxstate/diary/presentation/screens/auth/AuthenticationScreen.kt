package com.uxstate.diary.presentation.screens.auth

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.uxstate.diary.presentation.screens.auth.components.AuthenticationContent

@Destination
@RootNavGraph(start = true)



@Composable
fun AuthenticationScreen() {

    Scaffold(content = {

        AuthenticationContent(loadingState = false, onButtonClicked = {})

    })



}


