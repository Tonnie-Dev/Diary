package com.uxstate.diary.domain.model

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

    fun clearImagesToBeDeleted(){
        imagesToBeDeleted.clear()
    }
}


data class GalleryImage(val image:Uri, val remoteImagePath:String = "")