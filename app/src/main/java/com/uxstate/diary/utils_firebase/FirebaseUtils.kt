package com.uxstate.diary.utils_firebase

import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.uxstate.mongo.local.database.ImagesDatabase
import com.uxstate.mongo.local.entities.ImageToDelete
import com.uxstate.mongo.local.entities.ImageToUpload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun cleanUpCheck(scope: CoroutineScope, database: ImagesDatabase) {
    scope.launch(Dispatchers.IO) {

        val unUploadedImages = database.imageToUploadDao.getAllImages()

        unUploadedImages.forEach {

            image ->

            retryUploadingImageToFirebase(
                    imageToUpload = image,

                    //onSuccess to trigger removal of images from database
                    onSuccess = {
                        scope.launch(Dispatchers.IO) {
                            database.imageToUploadDao.cleanupImage(imageId = image.id)
                        }
                    })
        }

        val unDeletedItems = database.imageToDeleteDao.getAllImages()
        unDeletedItems .forEach {

            imageToDelete ->

            //call retry delete util function

            retryDeletingImageToFirebase(imageToDelete = imageToDelete, onSuccess = {
                scope.launch(Dispatchers.IO) {
                    //when we successfully remove images from FB, we cleanup database
                    database.imageToDeleteDao.cleanUpImage(imageToDelete.id)
                }

            })
        }
    }
}


private fun retryUploadingImageToFirebase(imageToUpload: ImageToUpload, onSuccess: () -> Unit) {

    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath)
            .putFile(
                    imageToUpload.imageUrl.toUri(),
                    storageMetadata { },
                    imageToUpload.sessionUrl.toUri()
            )

            //add onSuccess Listener instead of OnProgressListener
            .addOnSuccessListener { onSuccess() }
}

private fun retryDeletingImageToFirebase(imageToDelete: ImageToDelete, onSuccess: () -> Unit) {

    val storage = FirebaseStorage.getInstance().reference

    storage.child(imageToDelete.remotePath)
            .delete()
            //add onSuccess Listener instead of OnProgressListener
            .addOnSuccessListener { onSuccess() }
}
