package com.uxstate.diary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uxstate.diary.data.local.entity.ImageToUpload

@Dao
interface ImageToUploadDao {

    @Query("SELECT * FROM images_to_upload_table ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToUpload>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToUpload(imageToUpload: ImageToUpload)

    @Query("DELETE FROM images_to_upload_table WHERE id=:imageId")
    suspend fun cleanupImage(imageId: Int)
}