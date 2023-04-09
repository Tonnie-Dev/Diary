package com.uxstate.diary.presentation.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {


    var loadingState by mutableStateOf(false)
    private set

    fun setLoading(isLoading:Boolean    ){

        loadingState = isLoading
    }

    fun signInWithMongoAtlas(){
        
    }
}