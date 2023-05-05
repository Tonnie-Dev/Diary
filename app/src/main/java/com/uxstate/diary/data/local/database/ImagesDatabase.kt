package com.uxstate.diary.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uxstate.diary.data.local.dao.ImageToUploadDao
import com.uxstate.diary.data.local.database.ImagesDatabase.Companion.DATABASE_VERSION
import com.uxstate.diary.data.local.entity.ImageToUpload
import kotlin.math.exp

@Database(entities = [ImageToUpload::class], version = DATABASE_VERSION, exportSchema = false)
abstract class ImagesDatabase:RoomDatabase(){

    abstract val imageToUploadDao:ImageToUploadDao

    companion object {
        const val DATABASE_NAME = "images_db"
        const val DATABASE_VERSION = 1

    }
}