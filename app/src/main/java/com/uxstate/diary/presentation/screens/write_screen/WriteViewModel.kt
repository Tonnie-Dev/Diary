package com.uxstate.diary.presentation.screens.write_screen


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uxstate.diary.data.repository.MongoDB
import com.uxstate.diary.presentation.screens.navArgs
import com.uxstate.diary.presentation.screens.write_screen.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(handle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val diaryId = handle.navArgs<WriteScreenNavArgs>().id


    private fun fetchSelectedDiary() {


        if (diaryId != null) {


            viewModelScope.launch(IO) {


                //val diary =
                   // MongoDB.getSelectedDiary(diaryId = ObjectId.Companion.from(_uiState.value.selectedDiaryId))
            }
        }

    }


}