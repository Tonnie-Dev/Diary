package com.uxstate.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.uxstate.ui.R
import com.uxstate.model.Mood
import com.uxstate.ui.theme.LocalSpacing
import com.uxstate.util.toStringTime
import java.time.Instant

@Composable
internal fun DiaryHeader(moodName: String, time: Instant) {


    val spacing = LocalSpacing.current

    //value of returns the actual enum
    val mood by remember { mutableStateOf(Mood.valueOf(moodName)) }

    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .background(mood.containerColor)
                    .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {


        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                    modifier = Modifier.size(spacing.spaceMedium),
                    painter = painterResource(id = mood.icon),
                    contentDescription = stringResource(R.string.mood_icon)
            )

            Spacer(modifier = Modifier.width(spacing.spaceSmall))
            Text(
                    text = Mood.valueOf(moodName).name,
                    style = TextStyle(
                            color = mood.contentColor,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
            )
        }

        Text(
                /*text = SimpleDateFormat("hh:mm a", Locale.ROOT)
                        .format(Date.from(time)),*/
                text = time.toStringTime(),
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }
}