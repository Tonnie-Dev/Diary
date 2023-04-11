package com.uxstate.diary.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.uxstate.diary.R
import com.uxstate.diary.presentation.ui.theme.*

enum class Mood(
    @DrawableRes val icon: Int,
    contentColor: Color,
    containerColor: Color
) {
    //Enum Constants
    NEUTRAL(
            icon = R.drawable.neutral,
            contentColor = Color.Black,
            containerColor = NeutralColor
    ),
    HAPPY(
            icon = R.drawable.happy,
            contentColor = Color.Black,
            containerColor = HappyColor
    ),
    ANGRY(
            icon = R.drawable.angry,
            contentColor = Color.White,
            containerColor = AngryColor
    ),
    BORED(
            icon = R.drawable.bored,
            contentColor = Color.Black,
            containerColor = BoredColor
    ),
    CALM(
            icon = R.drawable.calm,
            contentColor = Color.Black,
            containerColor = CalmColor
    ),
    DEPRESSED(
            icon = R.drawable.depressed,
            contentColor = Color.Black,
            containerColor = DepressedColor
    ),
   DISAPPOINTED(
            icon = R.drawable.disappointed,
            contentColor = Color.White,
            containerColor = DisappointedColor
    ),
    HUMOROUS(
            icon = R.drawable.humorous,
            contentColor = Color.Black,
            containerColor = HumorousColor
    ),
    LONELY(
            icon = R.drawable.lonely,
            contentColor = Color.White,
            containerColor = LonelyColor
    ),
    MYSTERIOUS(
            icon = R.drawable.mysterious,
            contentColor = Color.Black,
            containerColor = MysteriousColor
    ),
    ROMANTIC(
            icon = R.drawable.romantic,
            contentColor = Color.White,
            containerColor = RomanticColor
    ),
    SHAMEFUL(
            icon = R.drawable.shameful,
            contentColor = Color.White,
            containerColor = ShamefulColor
    ),
    AWFUL(
            icon = R.drawable.awful,
            contentColor = Color.Black,
            containerColor = AwfulColor
    ),
    SURPRISED(
            icon = R.drawable.surprised,
            contentColor = Color.Black,
            containerColor = SurprisedColor
    ),
    SUSPICIOUS(
            icon = R.drawable.suspicious,
            contentColor = Color.Black,
            containerColor = SuspiciousColor
    ),
    TENSE(
            icon = R.drawable.tense,
            contentColor = Color.Black,
            containerColor = TenseColor
    )
}