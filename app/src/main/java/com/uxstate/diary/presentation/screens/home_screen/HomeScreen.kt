package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.R
import com.uxstate.diary.presentation.screens.destinations.WriteScreenDestination
import com.uxstate.diary.presentation.screens.home_screen.components.HomeTopBar
import com.uxstate.diary.presentation.screens.home_screen.components.DiaryNavigationDrawer

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {


    Scaffold(
            topBar = {
                HomeTopBar(onMenuClicked = {})
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


DiaryNavigationDrawer(drawerState = , onSignOutClicked = { /*TODO*/ }) {
    
}


}