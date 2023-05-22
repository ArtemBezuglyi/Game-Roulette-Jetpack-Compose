package ru.artbez.composeroulette.table_screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.artbez.composeroulette.MainActivity
import ru.artbez.composeroulette.R
import ru.artbez.composeroulette.ui.theme.GreenTable
import ru.artbez.composeroulette.ui.theme.RedButton
import ru.artbez.composeroulette.utils.*
import kotlin.math.roundToInt

private val String.isChosen: Boolean
    get() {return (toString() != noChoice) && (toString() != empty)}
private val String.isEmpty: Boolean
    get() {return (toString() == noStake) || (toString() == empty)}
private val String.isAllYourMoney: Boolean
    get() {return toString() == allIn}
private val Int.isReal: Boolean
    get() {return toInt() != -1}
private val Int.isPositive: Boolean
    get() {return toInt() > 0}
private val Int.isEven: Boolean
    get() {return toInt()%2 == 0}
private val Int.isOutOfNumberList: Boolean
    get() {return (toInt() <= 0) || (toInt() >= 37)}
private val Float.isOutOfListSize: Boolean
    get() {return (toFloat() < 0) || (toFloat() >= 36.5)}

@Composable
fun MainScreen() {

    val context = LocalContext.current

    var rouletteRotationValue by remember { mutableStateOf(0f) }

    var winningNumber by rememberSaveable { mutableStateOf(0) }
    var winningColor by rememberSaveable { mutableStateOf(green) }

    var textOfWinningColor by remember { mutableStateOf(empty) }
    var backgroundWinningColor by remember { mutableStateOf(Color.Green) }

    var yourMoneyAccount by rememberSaveable { mutableStateOf(1000) }
    var gameRecord by rememberSaveable { mutableStateOf(SharedPreferences.firstRecord()) }

    var colorBet by rememberSaveable { mutableStateOf(noChoice) }
    var numberBet by rememberSaveable { mutableStateOf(noChoice) }
    var moneyStake by rememberSaveable { mutableStateOf(noStake) }

    var numberBetDigit by rememberSaveable { mutableStateOf(100) }
    var moneyStakeDigit by rememberSaveable { mutableStateOf(0) }

    val yourMoneyNotEnoughFor = { param: String -> param.toInt() > yourMoneyAccount}

    var openGameOverDialog by remember { mutableStateOf(false) }

    fun setGreen() {
        winningColor = green
        textOfWinningColor = empty
        backgroundWinningColor = Color.Green
    }

    fun setBlack() {
        winningColor = black
        textOfWinningColor = " Black"
        backgroundWinningColor = Color.Black
    }

    fun setRed() {
        winningColor = red
        textOfWinningColor = " Red"
        backgroundWinningColor = Color.Red
    }

    fun getWinningNumber(angle: Float) {
        val floatIndexOfRouletteNumber = (360f - (angle % 360)) / (360f / RouletteNumbers.listOfRouletteNumbers.size)
        val rouletteNumberIndex = if (floatIndexOfRouletteNumber.isOutOfListSize) 0 else floatIndexOfRouletteNumber.roundToInt()
        winningNumber = RouletteNumbers.listOfRouletteNumbers[rouletteNumberIndex]
        if (winningNumber.isOutOfNumberList) setGreen()
        else
            if (rouletteNumberIndex.isEven) setBlack() else setRed()
    }

    fun calculateMoneyStake() {
        numberBetDigit = if (numberBet.isChosen) numberBet.toInt() else -1
        moneyStakeDigit = if (moneyStake.isEmpty) 0 else
            if (moneyStake.isAllYourMoney) yourMoneyAccount else
                if (yourMoneyNotEnoughFor(moneyStake)) yourMoneyAccount else moneyStake.toInt()

        if (((numberBetDigit.isReal) || (colorBet.isChosen)) && (moneyStakeDigit.isPositive)) {
            yourMoneyAccount -= moneyStakeDigit
            Toast.makeText(MAIN, "Your stake: $moneyStakeDigit", Toast.LENGTH_SHORT).show()
        }
    }

    fun yourWin() {
        var yourWin = 0
        if ((numberBetDigit.isReal) && (numberBetDigit == winningNumber)) yourWin += (moneyStakeDigit*5)
        if ((colorBet.isChosen) && (colorBet == winningColor)) yourWin += (moneyStakeDigit*2)
        if (yourWin.isPositive) {
            yourMoneyAccount += yourWin
            Toast.makeText(MAIN, "Your win: $yourWin!", Toast.LENGTH_SHORT).show()
        }
        if (yourMoneyAccount > gameRecord) {
            Toast.makeText(MAIN, "New record: $yourMoneyAccount!", Toast.LENGTH_SHORT).show()
            SharedPreferences.setRecord(yourMoneyAccount.toString())
            gameRecord = SharedPreferences.getRecord()!!.toInt()
        }
        if (!yourMoneyAccount.isPositive) openGameOverDialog = true
    }

    val angleOfRouletteRotation: Float by animateFloatAsState(
        targetValue = rouletteRotationValue,
        animationSpec = tween(
            durationMillis = 3000,
            easing = LinearOutSlowInEasing
        ),
        finishedListener = { resultAngle ->
            getWinningNumber(resultAngle)
            yourWin()
        }
    )

    fun spinTheRoulette() {
        //old version
        //rouletteRotationValue = ((0..360).random()).toFloat() + 720f + angleOfRouletteRotation
        rouletteRotationValue = complexRandom().toFloat() + twoSpins + angleOfRouletteRotation
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .wrapContentHeight()
                .wrapContentWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(color = backgroundWinningColor)
                .padding(vertical = 2.dp, horizontal = 8.dp),
            text = " $winningNumber$textOfWinningColor ",
            fontWeight = FontWeight.Bold,
            fontSize = 35.sp,
            color = Color.White,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Your score: $yourMoneyAccount",
                modifier = Modifier.padding(horizontal = 10.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Record: $gameRecord",
                modifier = Modifier.padding(horizontal = 10.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
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
                    .rotate(angleOfRouletteRotation)
            )
            Image(
                painter = painterResource(id = R.drawable.flecha),
                contentDescription = "win sign",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        colorBet = menuWithList(betFieldName = "Color bet (x2)", itemList = listOf(noChoice, black, red, green))
        numberBet = menuWithList(betFieldName = "Number bet (x5)", itemList = listOf(noChoice) + rouletteNumbers)
        moneyStake = menuWithList(betFieldName = "Your stake", itemList = listOf(noStake) + bets + listOf(allIn))
        Button(
            onClick = {
                calculateMoneyStake()
                spinTheRoulette()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = RedButton),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "Spin the roulette",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
        if (openGameOverDialog) {
            AlertDialog(
                onDismissRequest = { openGameOverDialog = false },
                title = {
                    Text(text = "GAME OVER!")
                        },
                text = {
                    Text(text = "Do you want to start again?")
                       },
                buttons = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 10.dp,
                            alignment = Alignment.End
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    MAIN.finish()
                                }
                                .border(
                                    width = 1.dp,
                                    color = RedButton,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(vertical = 5.dp, horizontal = 25.dp),
                        ) {
                            Text(
                                text = "NO",
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clickable {
                                    openGameOverDialog = false
                                    context.startActivity(Intent(MAIN, MainActivity::class.java))
                                    MAIN.finish()
                                }
                                .background(
                                    color = GreenTable,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(vertical = 5.dp, horizontal = 23.dp),
                        ) {
                            Text(
                                text = "YES",
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(1.dp))
                    }
                }
            )
        }
}