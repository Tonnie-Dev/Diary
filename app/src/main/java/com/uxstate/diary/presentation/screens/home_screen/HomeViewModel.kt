package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uxstate.data.repository.MongoDB
import com.uxstate.diary.domain.repository.Diaries
import com.uxstate.diary.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

   val  diaries = mutableStateOf<Diaries>(RequestState.Idle)

   init {
       Timber.i("Init Block Called")
       observeAllDiaries()
   }

    private fun observeAllDiaries() {
        viewModelScope.launch {


            MongoDB.getAllDiaries().collect{

                result ->

                diaries.value = result
                Timber.i("The Result is $result")
            }
        }
    }
}