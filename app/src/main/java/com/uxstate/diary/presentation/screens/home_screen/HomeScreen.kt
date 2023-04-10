package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.presentation.screens.home_screen.components.DiaryNavigationDrawer
import com.uxstate.diary.presentation.screens.home_screen.components.HomeScaffold
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    //drawerState.open is suspend
    val scope = rememberCoroutineScope()

    DiaryNavigationDrawer(
            drawerState = drawerState,
            content = {
                HomeScaffold(
                        navigator = navigator,
                        onMenuClicked = {
                                scope.launch {

                                        drawerState.open()
                                }

                        })
            },
            onSignOutClicked = { /*TODO*/ }
    )


}