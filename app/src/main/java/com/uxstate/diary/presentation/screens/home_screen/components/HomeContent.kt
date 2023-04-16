package com.uxstate.diary.presentation.screens.home_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.uxstate.diary.R
import com.uxstate.diary.domain.model.Diary
import com.uxstate.diary.presentation.ui.theme.LocalSpacing
import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(diaryNotes: Map<LocalDate, List<Diary>>, onClick: (id: String) -> Unit) {

    val spacing = LocalSpacing.current
    if (diaryNotes.isNotEmpty()) {

        LazyColumn(modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                content = {

                   diaryNotes.forEach { (localDate, diaries) ->

                       stickyHeader(key = localDate){
                           DateHeader(localDate = localDate)
                       }

                       items(items = diaries, key = {
                           it._id
                       }){

                           DiaryHolder(diary = it, onClickDiary = onClick)
                       }

                   }
                })


    } else {

        EmptyPage(
                title = stringResource(R.string.empty_diary_text),
                subtitle = stringResource(R.string.write_something_text)
        )
    }
}

@Composable
fun EmptyPage(title: String, subtitle: String ) {
    val spacing = LocalSpacing.current
    Column(
            modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing.spaceLarge),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
                text = title,
                style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Medium
                )
        )
        Text(
                text = subtitle,
                style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Normal
                )
        )
    }
}