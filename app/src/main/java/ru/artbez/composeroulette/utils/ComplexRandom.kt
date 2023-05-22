package ru.artbez.composeroulette.utils

import java.util.*

fun complexRandom() : Int {
    val result : Int
    val day = Date().time
    val num1 = day/347
    val num2 = day%347
    val num3 = (num1%2).toInt()
    val x : Int = if (num3 == 0)
        (1..4).random()
    else
        (5..8).random()

    result = when (x)
    {
        1 -> num2.toInt()
        2 -> num2.toInt() * 2
        3 -> num2.toInt() / 2
        5 -> num2.toInt() + 6
        6 -> num2.toInt() + 13
        7 -> num2.toInt() + 42
        else -> num2.toInt() + 108
    }
    return result
}