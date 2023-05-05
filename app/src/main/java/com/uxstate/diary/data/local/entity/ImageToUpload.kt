package com.uxstate.diary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uxstate.diary.util.Constants.IMAGE_TO_UPLOAD_TABLE


@Entity(tableName = IMAGE_TO_UPLOAD_TABLE)
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val remoteImagePath: String,
    val imageUrl: String,
    val sessionUrl: String
)
