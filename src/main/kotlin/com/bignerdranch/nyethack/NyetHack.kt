package com.bignerdranch.nyethack

const val TOO_MANY_SYMBOLS = 7
val player = Player()

fun main() {
    narrate("${player.name} is ${player.title}")
    player.changeName("dzaychok")

//    com.bignerdranch.nyethack.changeNarratorMood()
    narrate("${player.name}, ${player.title}, heads to the town square")
    visitTavern()
    player.castFireball()
}

//private fun makeYellow(message: String) = "\u001b[33;1m$message\u001b[0m"