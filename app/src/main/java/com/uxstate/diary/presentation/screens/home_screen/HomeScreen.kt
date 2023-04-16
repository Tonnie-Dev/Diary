package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.data.repository.MongoDB
import com.uxstate.diary.presentation.screens.destinations.AuthenticationScreenDestination
import com.uxstate.diary.presentation.screens.home_screen.components.DiaryNavigationDrawer
import com.uxstate.diary.presentation.screens.home_screen.components.DisplayAlertDialog
import com.uxstate.diary.presentation.screens.home_screen.components.HomeScaffold
import com.uxstate.diary.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import com.uxstate.diary.R

@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var isSignOutDialogOpen by remember { mutableStateOf(false) }

    //drawerState.open/signIn/signOut is suspend
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
            onSignOutClicked = { isSignOutDialogOpen = true }
    )


    LaunchedEffect(true, block ={

        MongoDB.configureTheRealm()
    } )

    //Park Alert Dialog to be triggered on isSignOutDialogOpen

    DisplayAlertDialog(
            title = stringResource(id = R.string.sign_out),
            message = stringResource(R.string.sign_out_confirmation_text),
            isDialogOpen = isSignOutDialogOpen,
            onCloseDialog = { isSignOutDialogOpen = false },
            onDialogConfirmed = {
                val user = App.Companion.create(APP_ID).currentUser
                if (user != null) {
                    scope.launch(IO) {

                        user.logOut()
                    }

                }
                isSignOutDialogOpen = false

                //pop out our home screen for security
                navigator.popBackStack()
                navigator.navigate(AuthenticationScreenDestination)
            })

}