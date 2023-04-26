package com.uxstate.diary.presentation.screens.write_screen

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.uxstate.diary.presentation.screens.navArgs
import com.uxstate.diary.presentation.screens.write_screen.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(handle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()


    init {
        val diaryId = handle.navArgs<WriteScreenNavArgs>().id
        _uiState.update {

            it.copy(selectedDiaryId = diaryId)
        }
    }



}