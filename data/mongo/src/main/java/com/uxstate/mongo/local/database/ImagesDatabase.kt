package com.uxstate.mongo.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uxstate.mongo.local.dao.ImageToDeleteDao
import com.uxstate.mongo.local.dao.ImageToUploadDao
import com.uxstate.mongo.local.database.ImagesDatabase.Companion.DATABASE_VERSION
import com.uxstate.mongo.local.entities.ImageToDelete
import com.uxstate.mongo.local.entities.ImageToUpload

@Database(entities = [ImageToUpload::class, ImageToDelete::class], version = DATABASE_VERSION, exportSchema = false)
abstract class ImagesDatabase:RoomDatabase(){

    abstract val imageToUploadDao: ImageToUploadDao
    abstract val imageToDeleteDao: ImageToDeleteDao

    companion object {
        const val DATABASE_NAME = "images_db"
        const val DATABASE_VERSION = 1

    }
}