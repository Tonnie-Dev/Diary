package com.uxstate.diary.presentation.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val spaceDefault: Dp = 0.dp,
    val spaceHalfDp: Dp = .5.dp,
    val spaceSingleDp: Dp = 1.dp,
    val spaceDoubleDp: Dp = 2.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceLargeMedium: Dp = 48.dp,
    val spaceExtraLarge: Dp = 64.dp,
    val spaceOneHundred: Dp = 100.dp,
    val spaceOneTwenty: Dp = 120.dp,
    val spaceOneHundredFifty: Dp = 150.dp,
    val spaceTwoHundred: Dp = 200.dp,
    val spaceTwoHundredFifty: Dp = 250.dp,
    val spaceFiveHundred: Dp = 500.dp

)

val LocalSpacing = compositionLocalOf { Dimens() }