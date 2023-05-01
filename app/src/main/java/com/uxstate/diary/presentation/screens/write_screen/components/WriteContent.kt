package com.uxstate.diary.presentation.screens.write_screen.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.uxstate.diary.R
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.domain.model.Mood
import com.uxstate.diary.presentation.screens.write_screen.state.UiState
import com.uxstate.diary.presentation.ui.theme.LocalSpacing

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WriteContent(
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    onSaveClicked: (diary:Diary) -> Unit,
    uiState: UiState,
    paddingValues: PaddingValues,
    pagerState: PagerState,

    ) {

    val scrollState = rememberScrollState()
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Column(
            modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(bottom = paddingValues.calculateBottomPadding())
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


            /*    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                       unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)*/
            TextField(
                    value = title,
                    onValueChange = onTitleChanged,
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
                    /*when focussed the ime action will be Next*/
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {}),
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
                    keyboardActions = KeyboardActions(onNext = {})
            )

        }

        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Button(
                    onClick = {

                        if (uiState.title.isNotEmpty() && uiState.description.isNotEmpty()) {

                            onSaveClicked(Diary().apply{
                                this.title = uiState.title
                                this.description = uiState.description
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