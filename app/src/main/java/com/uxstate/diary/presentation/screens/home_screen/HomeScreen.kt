package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.data.repository.MongoDB
import com.uxstate.diary.R
import com.uxstate.diary.presentation.keepSplashScreen
import com.uxstate.diary.presentation.screens.destinations.AuthenticationScreenDestination
import com.uxstate.diary.presentation.screens.home_screen.components.DiaryNavigationDrawer
import com.uxstate.diary.presentation.screens.home_screen.components.DisplayAlertDialog
import com.uxstate.diary.presentation.screens.home_screen.components.HomeScaffold
import com.uxstate.diary.util.Constants.APP_ID
import com.uxstate.diary.util.RequestState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val diaries by viewModel.diaries
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

                        }, diaries = diaries)
            },
            onSignOutClicked = { isSignOutDialogOpen = true }
    )


    //Launched Effect to control splash screen
    LaunchedEffect(key1 = diaries, block = {

        //when diaries are not loading
        if (diaries !is RequestState.Loading){

           keepSplashScreen = false

        }

    })


    LaunchedEffect(key1 = Unit, block = {
        MongoDB.configureTheRealm()

    })



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