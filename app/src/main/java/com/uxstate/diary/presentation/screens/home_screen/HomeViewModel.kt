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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivityObserver: ConnectivityObserver,
    private val database: ImagesDatabase
) : ViewModel() {

    private val _diaries = MutableStateFlow<Diaries>(RequestState.Idle)
    val diaries = _diaries.asStateFlow()

    var dateSelected by mutableStateOf(false)
        private set

    //to be changed whenever network status changes
    private var networkStatus by mutableStateOf(ConnectivityObserver.Status.UNAVAILABLE)

    /*This is to help us cancel jobs when we switch from allDiaries
    to filteredDiaries and vice-versa*/
    private lateinit var allDiariesJob: Job
    private lateinit var filteredDiariesJob: Job

    init {

        getDiaries()

        //observe network status and change status whenever there is a change
        viewModelScope.launch {
            connectivityObserver.observe()
                    .collect {
                        networkStatus = it
                    }
        }
    }

    //This functions determines if to pull all diaries or for a particular day
    fun getDiaries(zoneDateTime: ZonedDateTime? = null) {

        /* the data will only be passed if we select a date from home screen*/
        dateSelected = zoneDateTime != null

        /*in the meantime set the diaries status to loading*/

        _diaries.value = RequestState.Loading

        if (dateSelected && zoneDateTime != null) {

            observeFilteredDiaries(zoneDateTime)

        } else {

            observeAllDiaries()
        }
    }

    private fun observeAllDiaries() {


        //initialize allDiaries job
        allDiariesJob = viewModelScope.launch {

            //cancel preview coroutines that was observing filtered diaries
            if (::filteredDiariesJob.isInitialized) {

                filteredDiariesJob.cancelAndJoin()
            }

            MongoDB.getAllDiaries()
                    .collect {

                        result ->

                        _diaries.value = result

                    }
        }
    }

    private fun observeFilteredDiaries(zoneDateTime: ZonedDateTime) {

        //initialize filteredDiariesJob
        filteredDiariesJob = viewModelScope.launch(IO) {

            //cancel preview coroutines that was observing all diaries
            if (::allDiariesJob.isInitialized) {

                allDiariesJob.cancelAndJoin()
            }

            MongoDB.getFilteredDiaries(zoneDateTime)
                    .collect { result ->

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

        } else {

            //Specify a custom Exception
            onError(Exception("No Internet Connection"))
        }

    }
}