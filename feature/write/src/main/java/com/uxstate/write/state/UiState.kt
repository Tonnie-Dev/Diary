package com.uxstate.write.state

import com.uxstate.model.Diary
import com.uxstate.model.Mood
import io.realm.kotlin.types.RealmInstant

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.NEUTRAL,
    val updatedDateTime:RealmInstant? = null
)
