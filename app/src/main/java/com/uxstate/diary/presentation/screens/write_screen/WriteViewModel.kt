package com.uxstate.diary.presentation.screens.write_screen


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uxstate.diary.data.repository.MongoDB
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.domain.model.Mood
import com.uxstate.diary.presentation.screens.navArgs
import com.uxstate.diary.presentation.screens.write_screen.state.UiState
import com.uxstate.diary.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(handle: SavedStateHandle) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val diaryId = handle.navArgs<WriteScreenNavArgs>().id


    init {
        fetchSelectedDiary()
    }

    private fun fetchSelectedDiary() {


        if (diaryId != null) {


            viewModelScope.launch(IO) {

                /*
               - empty invoke() is used to generate a new ObjectId
               - if you pass in a hex value that will return an existing ObjectId
                */


                val diary =
                    MongoDB.getSelectedDiary(diaryId = ObjectId.invoke(diaryId))

                if (diary is RequestState.Success) {

                    setTitle(title = diary.data.title)

                    setDescription(description = diary.data.description)

                    setMood(mood = Mood.valueOf(diary.data.mood))

                    setSelectedDiary(diary =diary.data)

                }
            }
        }

    }

    fun setTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun setDescription(description: String) {

        _uiState.update { it.copy(description = description) }
    }

    private fun setMood(mood: Mood) {

        _uiState.update { it.copy(mood = mood) }
    }

    fun setSelectedDiary(diary: Diary) {
        _uiState.update { it.copy(selectedDiary = diary) }
    }

}