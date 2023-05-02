package com.uxstate.diary.domain.model

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

class GalleryState {

    val images = mutableStateListOf<GalleryImage>()
    val imagesToBeDeleted = mutableStateListOf<GalleryImage>()

    fun addImage(galleryImage: GalleryImage){

        images.add(galleryImage)
    }

    fun removeImage(galleryImage: GalleryImage){
        images.remove(galleryImage)
        imagesToBeDeleted.add(galleryImage)
    }

    fun clearImagesToBeDeleted(){
        imagesToBeDeleted.clear()
    }
}

/*
 - composable function with small letter as this compose returns something
 - remembers GalleryState across multiple recompositions
  - this will be used used within composable code to initilize the gallery state
 */

@Composable
fun rememberGalleryState(): GalleryState {

    return remember { GalleryState() }
}
data class GalleryImage(val image:Uri, val remoteImagePath:String = "")