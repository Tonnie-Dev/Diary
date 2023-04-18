package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.R
import com.uxstate.diary.domain.repository.Diaries
import com.uxstate.diary.presentation.screens.destinations.WriteScreenDestination
import com.uxstate.diary.util.RequestState
import timber.log.Timber

@Composable
fun HomeScaffold(diaries: Diaries, onMenuClicked: () -> Unit, navigator: DestinationsNavigator) {
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
            }


    ) {

        when (diaries) {

            is RequestState.Success -> {

                Timber.i("Inside Success Block ${diaries.data.isEmpty()}")
                HomeContent(diaryNotes = diaries.data, onClick = {})
            }

            is RequestState.Loading -> {
                Timber.i("Loading")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is RequestState.Error -> {
                Timber.i("Inside Error Block ${diaries.error.message}")
                EmptyPage(
                        title = stringResource(R.string.error_text),
                        subtitle = "${diaries.error.message}"
                )
            }

            else -> {}
        }


    }

}