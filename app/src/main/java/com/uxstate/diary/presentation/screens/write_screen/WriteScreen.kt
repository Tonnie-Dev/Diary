package com.uxstate.diary.presentation.screens.write_screen

import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.diary.R
import com.uxstate.diary.domain.model.GalleryImage
import com.uxstate.diary.domain.model.Mood
import com.uxstate.diary.domain.model.rememberGalleryState
import com.uxstate.diary.presentation.screens.write_screen.components.WriteContent
import com.uxstate.diary.presentation.screens.write_screen.components.WriteTopBar

@OptIn(ExperimentalPagerApi::class)
@Destination(navArgsDelegate = WriteScreenNavArgs::class)
@Composable
fun WriteScreen(viewModel: WriteViewModel = hiltViewModel(), navigator: DestinationsNavigator) {

    val state by viewModel.uiState.collectAsState()


    val pagerState = rememberPagerState()
    val pageNumber by remember { //buffer to prevent recomposition
        derivedStateOf { pagerState.currentPage }
    }

    /*   from gallery state model calls compose function to be
    able to call methods of GalleryState e.g. addImage*/
    val galleryState = rememberGalleryState()

    val context = LocalContext.current //Update the Mood when selecting an existing Diary
    LaunchedEffect(key1 = state.mood, block = {
        pagerState.scrollToPage(Mood.valueOf(state.mood.name).ordinal)

    })

    Scaffold(topBar = {
        WriteTopBar(

                selectedDiary = state.selectedDiary,
                moodName = { Mood.values()[pageNumber].name },
                onDeleteConfirmed = {


                    viewModel.deleteDiary(onSuccess = {
                        Toast.makeText(
                                context, R.string.diary_deleted_toast, Toast.LENGTH_SHORT
                        )
                                .show()

                        navigator.navigateUp()
                    }, onError = { message ->

                        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                .show()

                    })
                },
                onBackPressed = { navigator.navigateUp() },
                onUpdateDateTime = viewModel::updateDateTime
        )
    }, content = {

        WriteContent(title = state.title,
                onTitleChanged = viewModel::setTitle,
                description = state.description,
                onDescriptionChanged = viewModel::setDescription,
                paddingValues = it,
                pagerState = pagerState,
                uiState = state,
                galleryState = galleryState,
                onImageSelected = { uri ->
                    galleryState.addImage(GalleryImage(image = uri, remoteImagePath = ""))
                },
                onSaveClicked = { diary ->
                    viewModel.upsertDiary(diary = diary.apply {
                        mood = Mood.values()[pageNumber].name
                    }, onSuccess = {
                        navigator.navigateUp()
                    }, onError = { message ->

                        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                .show()
                    })
                })
    })
}


data class WriteScreenNavArgs(val id: String?)

