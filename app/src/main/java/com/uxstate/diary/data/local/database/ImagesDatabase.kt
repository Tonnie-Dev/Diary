package com.uxstate.diary.data.local.database

import androidx.room.Database
import com.uxstate.diary.data.local.dao.ImageToUploadDao
import kotlin.math.exp

@Database(entities = [ImagesDatabase::class], version = 1, exportSchema = false)
abstract class ImagesDatabase {

    abstract val imageToUploadDao:ImageToUploadDao
}