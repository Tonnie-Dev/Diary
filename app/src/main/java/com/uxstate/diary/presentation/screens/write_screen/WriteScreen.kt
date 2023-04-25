package com.uxstate.diary.presentation.screens.write_screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.presentation.screens.write_screen.components.WriteTopBar

@Destination
@Composable
fun WriteScreen(navigator: DestinationsNavigator) {

    Scaffold(topBar = {
        WriteTopBar(
                selectedDiary = null/*Diary().apply { title = "Light House"
                    description = "Some light in the Sear"
                }*/,
                onDeleteConfirmed = {},
                onBackPressed = { navigator.navigateUp() }


        )
    }, content = {})
}