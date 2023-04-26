package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.R
import com.uxstate.diary.domain.repository.Diaries
import com.uxstate.diary.presentation.screens.destinations.WriteScreenDestination
import com.uxstate.diary.util.RequestState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(diaries: Diaries, onMenuClicked: () -> Unit, navigator: DestinationsNavigator) {

    var padding by remember {
        mutableStateOf(PaddingValues())
    }

    //helps with animating TopAppBar
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    Scaffold(modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
            topBar = {
                HomeTopBar(onMenuClicked = onMenuClicked, scrollBehavior = scrollBehavior)
            },
            floatingActionButton = {
                FloatingActionButton(
                        onClick = { navigator.navigate(WriteScreenDestination(id = null)) },
                        modifier = Modifier.padding(
                                end = padding.calculateEndPadding(LayoutDirection.Ltr)
                        )
                ) {
                    Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit_text)
                    )
                }
            }


    ) { paddingValues ->


        when (diaries) {

            is RequestState.Success -> {


                HomeContent(
                        diaryNotes = diaries.data,
                        onClickDiary = {
                            navigator.navigate(WriteScreenDestination(it))

                        },
                        paddingValues = paddingValues
                )
            }

            is RequestState.Loading -> {
                Timber.i("Loading")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RequestState.Error -> {

                EmptyPage(
                        title = stringResource(R.string.error_text),
                        subtitle = "${diaries.error.message}"
                )
            }

            else -> {}
        }


    }

}