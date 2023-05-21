package com.uxstate.home.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.uxstate.ui.theme.DiaryTheme
import com.uxstate.ui.theme.LocalSpacing
import java.time.LocalDate


@Composable
fun DateHeader(localDate: LocalDate) {
    val spacing = LocalSpacing.current

    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(spacing.spaceMedium),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End) {

            Text(
                    text = String.format("%02d", localDate.dayOfMonth),
                    style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize),
                    fontWeight = FontWeight.Light
            )

            Text(
                    text = localDate.dayOfWeek.toString()
                            .take(3),
                    style = TextStyle(
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = FontWeight.Light
                    )
            )
        }
        Spacer(modifier = Modifier.width(spacing.spaceMedium))

        Column(horizontalAlignment = Alignment.Start) {


            Text(
                    text = localDate.month.toString()
                            .lowercase()
                            .replaceFirstChar { it.titlecase() },
                    style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Light
                    )

            )
            Spacer(modifier = Modifier.width(spacing.spaceMedium))

            Text(
                    text = "${localDate.year}",
                    style = TextStyle(

                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    ),

                    )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DateHeaderPrev() {
    DiaryTheme() {
        DateHeader(localDate = LocalDate.now())
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DateHeaderPrevDark() {


    DiaryTheme() {
        DateHeader(localDate = LocalDate.now())
    }
}