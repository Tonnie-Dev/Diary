package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.uxstate.diary.domain.model.Mood
import java.time.Instant

@Composable
fun DiaryHeader(moodName:String, time:Instant) {


    Row(modifier = Modifier.fillMaxWidth().background(Mood.valueOf(moodName).)) {

    }
}