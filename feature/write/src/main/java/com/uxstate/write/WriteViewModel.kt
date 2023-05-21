package com.uxstate.write


import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.uxstate.diary.presentation.screens.navArgs
import com.uxstate.diary.presentation.screens.write_screen.state.UiState
import com.uxstate.model.Diary
import com.uxstate.model.GalleryImage
import com.uxstate.model.GalleryState
import com.uxstate.model.Mood
import com.uxstate.mongo.local.database.ImagesDatabase
import com.uxstate.mongo.local.entities.ImageToDelete
import com.uxstate.mongo.local.entities.ImageToUpload
import com.uxstate.mongo.repository.MongoDB
import com.uxstate.util.RequestState
import com.uxstate.util.fetchImagesFromFirebase
import com.uxstate.util.toRealmInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import timber.log.Timber
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    handle: SavedStateHandle, private val database: ImagesDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val galleryState = GalleryState()
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
               - we also need to intercept the delete-error
                */



                MongoDB.getSelectedDiary(diaryId = ObjectId.invoke(diaryId))
                        .catch { emit(RequestState.Error(Exception("Diary is already Deleted"))) }
                        .collect {

                            diary ->
                            if (diary is RequestState.Success) {

                                setTitle(title = diary.data.title)

                                setDescription(description = diary.data.description)

                                setMood(mood = Mood.valueOf(diary.data.mood))

                                setSelectedDiary(diary = diary.data)


                                //fetch download url for display to the user

                                fetchImagesFromFirebase(remoteImagesPaths = diary.data.images,
                                        onImageDownload = { uri ->

                                            Timber.i("The Uri is: $uri")

                                            galleryState.addImage(
                                                    GalleryImage(
                                                            imageUri = uri,
                                                            remoteImagePath = extractRemoteImagePath(
                                                                    fullImageUrl = uri.toString()
                                                            )
                                                    )
                                            )
                                        },
                                        onImageDownloadFailed = {},
                                        onReadyToDisplay = {})

                            }

                        }


            }
        }

    }

    private fun extractRemoteImagePath(fullImageUrl: String): String {

        val chunks = fullImageUrl.split("%2F")
        val imageName = chunks[2].split("?")
                .first()

        return "images/${Firebase.auth.currentUser?.uid}/$imageName"
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

    private fun setSelectedDiary(diary: Diary) {
        _uiState.update { it.copy(selectedDiary = diary) }
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime?) {

        _uiState.update {
            if (zonedDateTime != null) {

                //change zonedDateTime to regular instant then toRealmInstant
                it.copy(
                        updatedDateTime = zonedDateTime.toInstant()
                                .toRealmInstant()
                )
            } else {

                it.copy(updatedDateTime = null)
            }

        }
    }

    fun upsertDiary(diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit) {

        if (_uiState.value.selectedDiary == null) {
            insertDiary(diary, onSuccess, onError)
        } else {
            updateDiary(diary, onSuccess, onError)
        }


    }

    private fun insertDiary(diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(IO) {
            val result = MongoDB.insertDiary(diary.apply {

                //Although this is a new diary, the user have tampered with the dates
                if (_uiState.value.updatedDateTime != null) {

                    this.date = _uiState.value.updatedDateTime!!
                }
            })

            withContext(Main) {

                processResult(result, onSuccess, onError)
            }

        }
    }

    private fun updateDiary(diary: Diary, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = MongoDB.updateDiary(diary = diary.apply {
                _id = ObjectId.invoke(diaryId!!)


                //checks if there is a new update
                if (_uiState.value.updatedDateTime != null) {

                    this.date = _uiState.value.updatedDateTime!!
                } else {

                    //extract the date from the diary itself
                    date = _uiState.value.selectedDiary!!.date
                }
            })


            withContext(Main) {
                processResult(result, onSuccess, onError)
            }


        }
    }


    fun deleteDiary(onSuccess: () -> Unit, onError: (String) -> Unit) {

        viewModelScope.launch(IO) {
            if (diaryId != null) {

                val result = MongoDB.deleteDiary(id = ObjectId.invoke(diaryId))

                when (result) {

                    is RequestState.Success -> {

                        withContext(Main) {

                            _uiState.value.selectedDiary?.let { //if a diary is deleted we also need to clear its files on firebase
                                deleteImagesFromFirebase(it.images)
                            }
                            onSuccess()
                        }
                    }

                    is RequestState.Error -> {

                        withContext(Main) {

                            onError(result.error.message ?: "Unknown Error")
                        }
                    }

                    else -> Unit
                }

            }

        }

    }


    fun addImage(image: Uri, imageType: String) {

        val remotePath =
            "images/" + "${Firebase.auth.currentUser?.uid}/" + "${image.lastPathSegment}-" + "${System.currentTimeMillis()}." + "imageType"



        Timber.i("The remotePath is $remotePath ")

        galleryState.addImage(GalleryImage(imageUri = image, remoteImagePath = remotePath))
    }


    private fun uploadImagesToFirebase() {

        //reference to Firebase to enable us create folder/path inside firebase
        val storage = FirebaseStorage.getInstance().reference

        //upload each imageUri on the GalleryState to FB storage
        galleryState.images.forEach { galleryImage ->

            val imagePath = storage.child(galleryImage.remoteImagePath)

            imagePath.putFile(galleryImage.imageUri) //add progress listener to putFile()
                    .addOnProgressListener {

                        val sessionUri = it.uploadSessionUri

                        //session uri expires after 7 days and can used to an upload later

                        if (sessionUri != null) {

                            /*
                            - if session uri is not null it means the progress has been interrupted
                            - we therefore persist the session uri
                            - database will be checked on each app launch to verify failed uploads
                            - if that is the case then we need to re-upload again.
                            */
                            viewModelScope.launch(IO) {
                                database.imageToUploadDao.addImageToUpload(
                                        imageToUpload = ImageToUpload(

                                                remoteImagePath = galleryImage.remoteImagePath,
                                                imageUrl = galleryImage.imageUri.toString(),
                                                sessionUrl = sessionUri.toString()
                                        )
                                )
                            }
                        }
                    }

        }
    }

    private fun deleteImagesFromFirebase(images: List<String>? = null) {
        val storage = FirebaseStorage.getInstance().reference


        if (images != null) {
            images.forEach { remotePath ->

                storage.child(remotePath)
                        .delete()
                        .addOnFailureListener {

                            viewModelScope.launch(IO) {

                                database.imageToDeleteDao.addImageToDelete(ImageToDelete(remotePath = remotePath))
                            }
                        }
            }
        } else {

            galleryState.imagesToBeDeleted.forEach { imageTobeDeleted ->

                storage.child(imageTobeDeleted.remoteImagePath)
                        .delete()
                        .addOnFailureListener {

                            viewModelScope.launch(IO) {

                                database.imageToDeleteDao.addImageToDelete(ImageToDelete(remotePath = imageTobeDeleted.remoteImagePath))
                            }
                        }
            }
        }

    }

    private fun <T> processResult(
        result: RequestState<T>, onSuccess: () -> Unit, onError: (String) -> Unit
    ) {

        when (result) {

            is RequestState.Success -> {

                uploadImagesToFirebase() //check if there are any images marked for deletion
                deleteImagesFromFirebase()

                //navigate back destroying the ViewModel
                onSuccess()
            }

            is RequestState.Error -> {
                onError(result.error.message ?: "Unknown Error Occurred")
            }

            else -> Unit
        }

    }
}