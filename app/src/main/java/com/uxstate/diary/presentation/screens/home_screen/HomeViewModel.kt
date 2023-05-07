package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uxstate.diary.connectivity.ConnectivityObserver
import com.uxstate.diary.data.repository.MongoDB
import com.uxstate.diary.domain.repository.Diaries
import com.uxstate.diary.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val connectivityObserver: ConnectivityObserver) : ViewModel() {

    private val _diaries = MutableStateFlow<Diaries>(RequestState.Idle)
   val  diaries = _diaries.asStateFlow()


   init {

       observeAllDiaries()
   }

    private fun observeAllDiaries() {
        viewModelScope.launch {


            MongoDB.getAllDiaries().collect{

                result ->

                _diaries.value = result

            }
        }
    }


    fun deleteAllDiaries( onSuccess:()-> Unit,  onError:(Throwable)-> Unit) {

    }
}