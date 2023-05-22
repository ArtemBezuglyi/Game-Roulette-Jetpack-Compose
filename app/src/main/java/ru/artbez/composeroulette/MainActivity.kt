package ru.artbez.composeroulette

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import ru.artbez.composeroulette.table_screen.MainScreen
import ru.artbez.composeroulette.ui.theme.COMPOSErouletteTheme
import ru.artbez.composeroulette.ui.theme.GreenTable
import ru.artbez.composeroulette.utils.MAIN

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MAIN = this
        setContent {
            COMPOSErouletteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GreenTable
                ) {
                    MainScreen()
                }
            }
        }
    }
}