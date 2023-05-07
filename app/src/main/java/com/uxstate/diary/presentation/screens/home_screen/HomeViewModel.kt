package com.uxstate.diary.presentation.screens.home_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.uxstate.diary.connectivity.ConnectivityObserver
import com.uxstate.diary.data.local.database.ImagesDatabase
import com.uxstate.diary.data.local.entities.ImageToDelete
import com.uxstate.diary.data.repository.MongoDB
import com.uxstate.diary.domain.repository.Diaries
import com.uxstate.diary.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val database: ImagesDatabase
) : ViewModel() {

    private val _diaries = MutableStateFlow<Diaries>(RequestState.Idle)
    val diaries = _diaries.asStateFlow()

    //to be changed whenever network status changes
    private var networkStatus by mutableStateOf(ConnectivityObserver.Status.UNAVAILABLE)


    init {

        observeAllDiaries()

        //observe network status and change status whenever there is a change
        viewModelScope.launch {
            connectivityObserver.observe()
                    .collect {
                        networkStatus = it
                    }
        }
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {


            MongoDB.getAllDiaries()
                    .collect {

                        result ->

                        _diaries.value = result

                    }
        }
    }

    /* As we are deleting the diaries permanently we need to check network status
    - we should only delete when we have network
    * */

    fun deleteAllDiaries(onSuccess: () -> Unit, onError: (Throwable) -> Unit) {

        if (networkStatus == ConnectivityObserver.Status.AVAILABLE) {

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            //get the FB image directory from the userId - use path {}

            val imageDirectory = "images/${userId}"

            val storage = FirebaseStorage.getInstance().reference

            storage.child(imageDirectory)
                    .listAll() //returns a Task - to list all images
                    .addOnSuccessListener { listingTask ->
                        //loop through each image located on FB storage
                        listingTask.items.forEach { imageRef ->

                            //retrieve FB remote path name to save into DB
                            val imageRemotePath = "images/${userId}/${imageRef.name}"

                            //we are now interested with Failure
                            storage.child(imageRemotePath)
                                    .delete()
                                    //if we fail to delete any image, we persist it in the DB
                                    .addOnFailureListener {

                                        viewModelScope.launch(IO) {

                                            database.imageToDeleteDao.addImageToDelete(
                                                    ImageToDelete(
                                                            remotePath = imageRemotePath
                                                    )
                                            )

                                        }
                                    }
                        }

                        /* only after going through the loop that we can call MongoDB
                        handle its case */
                        viewModelScope.launch(IO) {
                            val result = MongoDB.deleteAllDiaries()

                            when (result) {

                                is RequestState.Success -> {

                                    withContext(Main) {

                                        onSuccess()
                                    }
                                }

                                is RequestState.Error -> {

                                    withContext(Main) {

                                        onError(result.error)
                                    }
                                }

                                else -> Unit
                            }
                        }
                        //on failing to list all the images
                    }
                    .addOnFailureListener {

                        onError(it)
                    }

        }

    }
}