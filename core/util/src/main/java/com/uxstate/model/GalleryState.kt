package com.uxstate.model

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf


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


}

/*
 - composable function with small letter as this compose returns something
 - remembers GalleryState across multiple recompositions
  - this will be used used within composable code to initilize the gallery state
 */

/*@Composable
fun rememberGalleryState(): GalleryState {

    return remember { GalleryState() }
}*/
data class GalleryImage(val imageUri:Uri, val remoteImagePath:String = "")