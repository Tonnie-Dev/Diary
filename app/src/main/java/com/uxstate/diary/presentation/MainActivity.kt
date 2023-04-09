package com.uxstate.diary.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ramcosta.composedestinations.DestinationsNavHost
import com.uxstate.diary.presentation.screens.NavGraphs
import com.uxstate.diary.presentation.ui.theme.DiaryTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
//xCVZ62a1SUMhANpf Tonnie
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //from Splash API
        installSplashScreen()
        setContent {
            DiaryTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)

            }
        }
    }
}
