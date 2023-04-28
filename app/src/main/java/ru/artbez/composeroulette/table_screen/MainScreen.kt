package ru.artbez.composeroulette.table_screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.artbez.composeroulette.R
import ru.artbez.composeroulette.ui.theme.RedBG
import ru.artbez.composeroulette.utils.Numbers
import kotlin.math.roundToInt

@Composable
fun MainScreen() {

    var rotationValue by remember {
        mutableStateOf(0f)
    }

    var number by remember {
        mutableStateOf(0)
    }

    var bgColor by remember {
        mutableStateOf("")
    }

    var color by remember { mutableStateOf(Color.Green) }


    val angle: Float by animateFloatAsState(
        targetValue = rotationValue,
        animationSpec = tween(
            durationMillis = 3000,
           easing = LinearOutSlowInEasing
        ),
        finishedListener = {
            val index = (360f - (it % 360)) / (360f / Numbers.list.size)
            val indexInt = if ((index <= 0) || (index >= 36.5)) 0 else index.roundToInt()
            number = Numbers.list[indexInt]
            if ((number <= 0) || (number >= 37)) {
                bgColor = ""
                color = Color.Green
            }
            else
                if (index.roundToInt()%2 == 0) {
                    bgColor = " Black"
                    color = Color.Black
                }
                else {
                    bgColor = " Red"
                    color = Color.Red

                }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .wrapContentHeight()
                .wrapContentWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(color = color)
                .padding(top = 2.dp, bottom = 2.dp, start = 8.dp, end = 8.dp),
            text = " $number$bgColor ",
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            color = Color.White,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ruleta),
                contentDescription = "roulette",
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(angle)
            )
            Image(
                painter = painterResource(id = R.drawable.flecha),
                contentDescription = "sign",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Button(
            onClick = {
                      rotationValue = ((0..360).random()).toFloat() + 720f + angle

            },
            colors = ButtonDefaults.buttonColors(backgroundColor = RedBG),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Start",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }


    }
}