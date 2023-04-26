package com.uxstate.diary.presentation.screens.write_screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.presentation.screens.write_screen.components.WriteContent
import com.uxstate.diary.presentation.screens.write_screen.components.WriteTopBar
import timber.log.Timber

@OptIn(ExperimentalPagerApi::class)
@Destination(navArgsDelegate = WriteScreenNavArgs::class)
@Composable
fun WriteScreen(viewModel: WriteViewModel = hiltViewModel(), navigator: DestinationsNavigator) {

    val uiState by viewModel.uiState.collectAsState()

    val pagerSate = rememberPagerState()


    LaunchedEffect(key1 = Unit, block = {

        Timber.i("The id is: ${uiState.selectedDiaryId} ")
        Timber.i("The id is a String: ${uiState.selectedDiaryId is String} ")
    })


    Scaffold(topBar = {
        WriteTopBar(
                selectedDiary = null/*Diary().apply { title = "Light House"
                    description = "Some light in the Sear"
                }*/,
                onDeleteConfirmed = {},
                onBackPressed = { navigator.navigateUp() }


        )
    }, content = {

        WriteContent(
                title = "",
                onTitleChanged = {},
                description = "",
                onDescriptionChanged = {},
                paddingValues = it,
                pagerState = pagerSate
        )
    })
}


data class WriteScreenNavArgs(val id: String?)