package ru.artbez.composeroulette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import ru.artbez.composeroulette.table_screen.MainScreen
import ru.artbez.composeroulette.ui.theme.COMPOSErouletteTheme
import ru.artbez.composeroulette.ui.theme.GreenBG

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            COMPOSErouletteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GreenBG
                ) {
                    MainScreen()
                }
            }
        }
    }
}