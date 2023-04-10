package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.R
import com.uxstate.diary.presentation.screens.destinations.WriteScreenDestination
import com.uxstate.diary.presentation.screens.home_screen.components.HomeTopBar

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {


    Scaffold(
            topBar = {
                HomeTopBar(onMenuClicked = {})
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {navigator.navigate(WriteScreenDestination)}) {
                    Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_text)
                    )
                }
            },
            content = {})

    /*  val scope = rememberCoroutineScope()
      Column(
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxSize()
      ) {

          Button(onClick = {

              scope.launch {
                  App.create(APP_ID).currentUser?.logOut()
                  navigator.navigate(AuthenticationScreenDestination())

              }

          }) {

              Text(text = "Log Out")
          }
      }*/

}