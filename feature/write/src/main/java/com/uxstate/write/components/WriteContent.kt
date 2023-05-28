package com.uxstate.write.components

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.uxstate.model.Diary
import com.uxstate.model.GalleryImage
import com.uxstate.model.GalleryState
import com.uxstate.model.Mood
import com.uxstate.ui.theme.LocalSpacing
import com.uxstate.write.state.UiState
import kotlinx.coroutines.launch
import com.uxstate.ui.R
import io.realm.kotlin.ext.toRealmList

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun WriteContent(
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    onSaveClicked: (diary: Diary) -> Unit,
    uiState: UiState,
    paddingValues: PaddingValues,
    pagerState: PagerState,
    galleryState: GalleryState,
    onImageSelected: (Uri) -> Unit,
    onImageClicked:(GalleryImage)-> Unit
) {

    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    //LaunchedEffect to scroll to the end of the text
    LaunchedEffect(key1 = scrollState.maxValue, block = {

        scrollState.scrollTo(scrollState.maxValue)
    })


    //we don't need start, end, bottom if we are using navigationBarsPadding
    Column(
            modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(spacing.spaceMedium)
                    .padding(horizontal = spacing.spaceMedium),

            verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
                modifier = Modifier
                        .weight(1f)
                        .verticalScroll(state = scrollState)
        ) {

            Spacer(modifier = Modifier.height(spacing.spaceLarge))

            HorizontalPager(state = pagerState, count = Mood.values().size) { page: Int ->


                val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                                .data(Mood.values()[page].icon)
                                .crossfade(true)
                                .build()
                )

                Image(
                        painter = painter,
                        contentDescription = "Mood Image",
                        modifier = Modifier.size(spacing.spaceOneTwenty)
                )


            }

            Spacer(modifier = Modifier.height(spacing.spaceMedium))



            TextField(
                    value = title,
                    onValueChange = onTitleChanged,
                    modifier = Modifier
                            .focusRequester(focusRequester)
                    /*.onGloballyPositioned {
                        focusRequester.requestFocus() // IMPORTANT
                    }*/,

                    placeholder = { Text(text = stringResource(R.string.title_text)) },
                    colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Unspecified,
                            disabledIndicatorColor = Color.Unspecified,
                            unfocusedIndicatorColor = Color.Unspecified,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.38f
                            )

                    ),
                    /*when focussed the ime action will be Next */
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(

                            onNext = {

                                scope.launch {

                                    scrollState.animateScrollTo(Int.MAX_VALUE)
                                    //- switch focus to next line
                                    focusManager.moveFocus(FocusDirection.Down)
                                }

                            }
                    ),
                    maxLines = 1,
                    singleLine = true
            )

            TextField(
                    value = description,
                    onValueChange = onDescriptionChanged,
                    placeholder = { Text(text = stringResource(R.string.desc_placeholder_text)) },
                    colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Unspecified,
                            disabledIndicatorColor = Color.Unspecified,
                            unfocusedIndicatorColor = Color.Unspecified,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.38f
                            )
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.clearFocus() })
            )

        }

        Column(verticalArrangement = Arrangement.Bottom) {


            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            GalleryUploader(
                    galleryState = galleryState,
                    onAddClicked = { focusManager.clearFocus() },
                    onImageSelected = onImageSelected,
                    onImageClicked = onImageClicked
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Button(
                    onClick = {

                        if (uiState.title.isNotEmpty() && uiState.description.isNotEmpty()) {

                            onSaveClicked(Diary().apply {
                                this.title = uiState.title
                                this.description = uiState.description
                                this.images =
                                    galleryState.images.map { galleryImage -> galleryImage.remoteImagePath }
                                            .toRealmList()
                            })
                        } else {

                            Toast.makeText(context, R.string.empty_field_text, Toast.LENGTH_SHORT)
                                    .show()
                        }
                    },
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(spacing.spaceLargeMedium + spacing.spaceSmall),
                    shape = Shapes().small
            ) {
                Text(text = stringResource(R.string.save_action))
            }
        }
    }
}