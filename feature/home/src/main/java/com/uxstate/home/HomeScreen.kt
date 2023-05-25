package com.uxstate.home

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.uxstate.home.components.DiaryNavigationDrawer
import com.uxstate.ui.components.DisplayAlertDialog
import com.uxstate.home.components.HomeScaffold
import com.uxstate.mongo.repository.MongoDB
import com.uxstate.util.Constants.APP_ID
import com.uxstate.util.RequestState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import com.uxstate.ui.R

// TODO: Remove drawables from app modules except ic_launcher logos

interface HomeScreenNavigator {

    fun navigateToWriteScreen(id:String?)
    fun navigateBackToAuthScreen()

    fun popBackStack()
}

// TODO: Revisit de-sugaring error API 26
@RequiresApi(Build.VERSION_CODES.O)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: HomeScreenNavigator
) {

    val diaries by viewModel.diaries.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var isSignOutDialogOpen by remember { mutableStateOf(false) }
    var isDeleteAllDialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current

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

                        },
                        diaries = diaries,


                        dateIsSelected = viewModel.dateSelected,

                        //pass the selected date
                        onDateSelected =viewModel::getDiaries,

                        //pass empty date to getDiaries ViewModel function
                        onDateReset = viewModel::getDiaries
                )
            },
            onSignOutClicked = { isSignOutDialogOpen = true },
            onDeleteAllDiaries = {

                isDeleteAllDialogOpen = true
            }
    )


    //Launched Effect to control splash screen
    LaunchedEffect(key1 = diaries, block = {

        // TODO: fix keepSplashScreen
       /* //when diaries are not loading
        if (diaries !is RequestState.Loading) {

            keepSplashScreen = false

        }*/

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
                val user = App.create(APP_ID).currentUser
                if (user != null) {
                    scope.launch(IO) {

                        user.logOut()
                    }

                }
                isSignOutDialogOpen = false

                //pop out our home screen for security
                navigator.popBackStack()
                navigator.navigateBackToAuthScreen()
            })


    DisplayAlertDialog(
            title = stringResource(R.string.delete_diaries_dialog_title),
            message = stringResource(R.string.delete_all_diaries_confirmation),
            isDialogOpen = isDeleteAllDialogOpen,
            onCloseDialog = { isDeleteAllDialogOpen = false },
            onDialogConfirmed = {
                viewModel.deleteAllDiaries(onSuccess = {
                    Toast.makeText(
                            context,
                            R.string.all_diaries_deleted_text,
                            Toast.LENGTH_SHORT
                    )
                            .show()

                    //close drawer

                    scope.launch {
                        drawerState.close()
                    }
                },
                        onError = {

                            val message = if (it.message == "No Internet Connection")
                                "We need an internet connection for this operation" else it.message

                            Toast.makeText(
                                    context,
                                    message,
                                    Toast.LENGTH_SHORT
                            )
                                    .show()

                        })

                scope.launch {
                    drawerState.close()
                }

            }
    )

}