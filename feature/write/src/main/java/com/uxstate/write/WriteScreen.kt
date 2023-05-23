package com.uxstate.write

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.uxstate.model.GalleryImage
import com.uxstate.model.Mood
import com.uxstate.write.components.WriteContent
import com.uxstate.write.components.WriteTopBar
import com.uxstate.write.components.ZoomableImage
import com.uxstate.ui.R

// TODO: Fix Require Api 26
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Destination(navArgsDelegate = WriteScreenNavArgs::class)
@Composable
fun WriteScreen(viewModel: WriteViewModel = hiltViewModel(), navigator: DestinationsNavigator) {

    //use by keyword to extract value from state holder
    var selectedGalleryImage by remember { mutableStateOf<GalleryImage?>(null) }
    val state by viewModel.uiState.collectAsState()


    val pagerState = rememberPagerState()
    val pageNumber by remember { //buffer to prevent recomposition
        derivedStateOf { pagerState.currentPage }
    }

    /*   from gallery state model calls compose function to be
    able to call methods of GalleryState e.g. addImage*/
    val galleryState = viewModel.galleryState

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
    }, content = { paddingValues ->

        WriteContent(title = state.title,
                onTitleChanged = viewModel::setTitle,
                description = state.description,
                onDescriptionChanged = viewModel::setDescription,
                paddingValues = paddingValues,
                pagerState = pagerState,
                uiState = state,
                galleryState = galleryState,
                onImageSelected = { uri ->

                    //content://media/picker/0/com.android.providers.media.photopicker/media/37
                    /*
                    - dynamic retrieval of uri ext,
                    - this uri is from the PhotoPicker activity contract
                    - this comes with '/' which needs to be isolated

                    */


                    val type = context.contentResolver.getType(uri)
                            ?.split("/")
                            ?.last() ?: "jpg"

                    viewModel.addImage(image = uri, imageType = type)


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
                }, onImageClicked = { galleryImage ->

            //update value for selectedGalleryImage
            selectedGalleryImage = galleryImage

        })

        //show/hide zoomable images
        AnimatedVisibility(visible = selectedGalleryImage != null) {
            /*
            - modal dialog to enable us draw on top of content
             - onDismiss executes when the user tries to dismiss dialog
              - this will switch the selectedGalleryImage state to null
             */

            Dialog(onDismissRequest = { selectedGalleryImage = null }) {


                if (selectedGalleryImage != null) {

                    ZoomableImage(
                            selectedGalleryImage = selectedGalleryImage!!,
                            onCloseClicked = { selectedGalleryImage = null },
                            onDeleteClicked = {
                                //recheck the image is not null again
                                if (selectedGalleryImage != null) {

                                    galleryState.removeImage(selectedGalleryImage!!)

                                    //after deleting the image switch it back to null
                                    selectedGalleryImage = null
                                }

                            })
                }
            }
        }
    })
}


data class WriteScreenNavArgs(val id: String?)

