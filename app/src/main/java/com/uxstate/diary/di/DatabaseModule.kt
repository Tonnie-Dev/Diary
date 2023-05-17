package com.uxstate.diary.di

import android.content.Context
import androidx.room.Room
import com.uxstate.mongo.local.database.ImagesDatabase
import com.uxstate.mongo.local.database.ImagesDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {

    companion object{
        @Provides
        @Singleton
        fun provideImagesDatabase(@ApplicationContext context: Context): ImagesDatabase {

            return Room.databaseBuilder(context, ImagesDatabase::class.java, DATABASE_NAME)
                    .build()
        }

    }

}