package com.uxstate.mongo.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uxstate.util.Constants.IMAGE_TO_UPLOAD_TABLE


@Entity(tableName = IMAGE_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteImagePath: String,
    val imageUrl: String,
    val sessionUrl: String
)
