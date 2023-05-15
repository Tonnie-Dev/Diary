package com.uxstate.util

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.uxstate.mongo.local.entities.ImageToDelete
import com.uxstate.mongo.local.entities.ImageToUpload
import io.realm.kotlin.types.RealmInstant
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun RealmInstant.toInstant(): Instant {

    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond

    return if (sec >= 0)
        Instant.ofEpochSecond(sec, nano.toLong())
    else
        Instant.ofEpochSecond((sec - 1), 1_000_000 + nano.toLong())
}


fun Instant.toStringTime(): String {

    val localDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())

    val pattern = "hh:mm a"

    //Java Time API formatter
    val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    return localDateTime.format(dateTimeFormatter)
}


fun Instant.toStringDateTime(): String {

    val localDateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())

    val pattern = "dd MMM yyyy hh:mm a"

    val dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    return localDateTime.format(dateTimeFormatter)

}

fun Instant.toRealmInstant(): RealmInstant {

    val sec: Long = this.epochSecond
    val nano: Int = this.nano

    return if (sec >= 0) {

        RealmInstant.from(sec, nano)


    } else {

        RealmInstant.from(epochSeconds = sec + 1, nanosecondAdjustment = -1_000_000 + nano)
    }
}


fun LocalDateTime.toStringDateTime(): String {

    val pattern = "dd MMM yyyy hh:mm a"
    val formatter = DateTimeFormatter.ofPattern(pattern)

    return this.format(formatter)

}

fun LocalDate.toStringDate(): String {

    val pattern = "dd MMM yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}


fun LocalTime.toStringTime(): String {

    val pattern = "hh:mm a"

    val formatter = DateTimeFormatter.ofPattern(pattern)

    return this.format(formatter)
}


/*

- Download images from Firebase asynchronously
 - This functions returns imageUri after each successfull download
*/
fun fetchImagesFromFirebase(
    remoteImagesPaths: List<String>,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed: (Exception) -> Unit = {},
    onReadyToDisplay: () -> Unit = {}
) {


    if (remoteImagesPaths.isNotEmpty()) {

        remoteImagesPaths.forEachIndexed { index, remoteImagePath ->


            if (remoteImagePath.trim()
                        .isNotEmpty()
            ) {

                FirebaseStorage.getInstance()
                        .reference.child(remoteImagePath.trim())
                        .downloadUrl //extracting the download url from storage reference
                        .addOnSuccessListener {
                            onImageDownload(it)


                            //triggered on reaching the last image
                            if (remoteImagesPaths.lastIndexOf(remoteImagesPaths.last()) == index) {
                                onReadyToDisplay()

                            }

                        }
                        .addOnFailureListener {

                            onImageDownloadFailed(Exception(it))
                        }
            }
        }
    }
}

fun retryUploadingImageToFirebase(imageToUpload: ImageToUpload, onSuccess: () -> Unit) {

    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath)
            .putFile(
                    imageToUpload.imageUrl.toUri(),
                    storageMetadata { },
                    imageToUpload.sessionUrl.toUri()
            )

            //add onSuccess Listener instead o OnProgressListener
            .addOnSuccessListener { onSuccess() }
}

fun retryDeletingImageToFirebase(imageToDelete: ImageToDelete, onSuccess: () -> Unit) {

    val storage = FirebaseStorage.getInstance().reference

    storage.child(imageToDelete.remotePath)
            .delete()
            //add onSuccess Listener instead of OnProgressListener
            .addOnSuccessListener { onSuccess() }
}





