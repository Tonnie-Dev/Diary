package com.uxstate.diary.presentation.screens.write_screen.state

import com.uxstate.diary.domain.model.Mood

data class UiState(
    val selectedDiaryId: String? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.NEUTRAL
)
