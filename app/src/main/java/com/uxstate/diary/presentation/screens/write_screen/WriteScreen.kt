package com.uxstate.diary.presentation.screens.write_screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.domain.model.Mood
import com.uxstate.diary.presentation.screens.write_screen.components.WriteContent
import com.uxstate.diary.presentation.screens.write_screen.components.WriteTopBar

@OptIn(ExperimentalPagerApi::class)
@Destination(navArgsDelegate = WriteScreenNavArgs::class)
@Composable
fun WriteScreen(viewModel: WriteViewModel = hiltViewModel(), navigator: DestinationsNavigator) {

    val state by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState()
    val pageNumber by remember {
        //buffer to prevent recomposition
        derivedStateOf { pagerState.currentPage }
    }


    //Update the Mood when selecting an existing Diary
    LaunchedEffect(key1 = state.mood, block = {
        pagerState.scrollToPage(Mood.valueOf(state.mood.name).ordinal)

    })


    Scaffold(topBar = {
        WriteTopBar(

                selectedDiary = state.selectedDiary,
                moodName = { Mood.values()[pageNumber].name },
                onDeleteConfirmed = {},
                onBackPressed = { navigator.navigateUp() }

        )
    }, content = {

        WriteContent(
                title = state.title,
                onTitleChanged = viewModel::setTitle,
                description = state.description,
                onDescriptionChanged = viewModel::setDescription,
                paddingValues = it,
                pagerState = pagerState,
                uiState = state,
                onSaveClicked = { diary ->
                    viewModel.insertDiary(diary = diary.apply {
                        mood = Mood.values()[pageNumber].name
                    },
                            onSuccess = {
                                navigator.navigateUp()
                            },
                            onError = {})
                }
        )
    })
}


data class WriteScreenNavArgs(val id: String?)

