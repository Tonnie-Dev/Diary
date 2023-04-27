package com.uxstate.diary.domain.repository

import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.util.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
interface MongoRepository    {

    fun configureTheRealm()
    fun getAllDiaries(): Flow<Diaries>

    fun getSelectedDiary(diaryId:ObjectId):RequestState<Diary>

    suspend fun addNewDiary(diary: Diary):RequestState<Diary>
}