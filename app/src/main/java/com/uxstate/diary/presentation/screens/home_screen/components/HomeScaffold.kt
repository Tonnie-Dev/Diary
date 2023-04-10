package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.R
import com.uxstate.diary.presentation.screens.destinations.WriteScreenDestination

@Composable
fun HomeScaffold(navigator:DestinationsNavigator, onMenuClicked:()-> Unit) {
    Scaffold(
            topBar = {
                HomeTopBar(onMenuClicked = onMenuClicked)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigator.navigate(WriteScreenDestination) }) {
                    Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_text)
                    )
                }
            },
            content = {}


    )

}