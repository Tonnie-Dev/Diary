package com.uxstate.diary.presentation.screens.HomeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.presentation.screens.destinations.AuthenticationScreenDestination
import com.uxstate.diary.presentation.screens.destinations.HomeScreenDestination
import com.uxstate.diary.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {

    val scope = rememberCoroutineScope()
    Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {

            scope.launch {
                App.create(APP_ID).currentUser?.logOut()
                navigator.navigate(AuthenticationScreenDestination())

            }

        }) {

            Text(text = "Log Out")
        }
    }

}