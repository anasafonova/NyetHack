package com.bignerdranch.nyethack

const val TOO_MANY_SYMBOLS = 7
lateinit var player: Player

fun main() {
//    if (::player.isInitialized) {
    narrate("Welcome to NyetHack!") //, ${player.name}!")
//    }
    val playerName = promptHeroName()

    player = Player(
        initialName = playerName,
        hometown = "Istra",
        healthPoints = 1000,
        isImmortal = true
    )

    narrate("${player.name} is ${player.title}")
    player.changeName("dzaychok")

//    com.bignerdranch.nyethack.changeNarratorMood()
    player.prophesize()
    val mortality = if (player.isImmortal) "an immortal" else "a mortal"
    narrate("${player.name} of ${player.hometown}, ${player.title}, heads to the town square")
    narrate("${player.name}, $mortality, has ${player.healthPoints} health points")

    visitTavern()
    player.castFireball()
    player.prophesize()
}

private fun promptHeroName(): String {
    narrate(
        "A hero enters the town of Kronstadt. What is their name?") { message ->
        "\u001b[33;1m$message\u001b[0m"
    }

//    val heroName = readLine()
//    require(heroName != null && heroName.isNotEmpty()) {
//        "The hero must have a name."
//    }
//    return heroName

    println("Dzaychok")
    return "Dzaychok"
}

//private fun makeYellow(message: String) = "\u001b[33;1m$message\u001b[0m"