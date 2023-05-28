package com.uxstate.mongo.repository

import com.uxstate.model.Diary
import com.uxstate.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime
typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
internal interface MongoRepository    {

    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>
    fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>>
    fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries>
    suspend fun insertDiary(diary: Diary): RequestState<Diary>
    suspend fun updateDiary(diary: Diary):RequestState<Diary>
    suspend fun deleteDiary(id: ObjectId):RequestState<Diary>

    suspend fun deleteAllDiaries():RequestState<Boolean>

}