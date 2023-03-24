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

    //    com.bignerdranch.nyethack.changeNarratorMood()

    Game.play()
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

object Game {
    private var isLaunched: Boolean = false

    private val worldMap = listOf(
        listOf(TownSquare(), Tavern(), Room("Back Room")),
        listOf(Room("A Long Corridor"), Room("A Generic Room")),
        listOf(Room("The Dungeon"))
    )

    private var currentRoom: Room = worldMap[0][0]
    private var currentPosition = Coordinate(0, 0)
    init {
        isLaunched = true
        narrate("Welcome, adventurer")
        narrate("${player.name} is ${player.title}")
        player.prophesize()

        val mortality = if (player.isImmortal) "an immortal" else "a mortal"
        narrate("${player.name}, $mortality, has ${player.healthPoints} health points")

        player.castFireball()
        player.prophesize()
    }

    fun play() {
        while (isLaunched) {
            narrate("${player.name} of ${player.hometown}, ${player.title}, is in ${currentRoom.description()}")
            currentRoom.enterRoom()

            print("> Enter your command: ")
            GameInput(readLine()).processCommand()
        }
    }

    fun move(direction: Direction) {
        val newPosition = direction.updateCoordinate(currentPosition)
        val newRoom = worldMap.getOrNull(newPosition.y)?.getOrNull(newPosition.x)

        if (newRoom != null) {
            narrate("The hero moves ${direction.name}")
            currentPosition = newPosition
            currentRoom = newRoom
        } else {
            narrate("You cannot move ${direction.name}")
        }
    }

    fun castSpell(spell: String) {
        when (spell) {
            "fireball" -> player.castFireball()
            else -> narrate("You cannot cast $spell")
        }
    }

    fun prophesize() {
        player.prophesize()
    }

    fun map() {
        val asciiMap = worldMap.mapIndexed { y, row ->
            row.mapIndexed { x, room ->
                when (currentPosition) {
                    Coordinate(x, y) -> "X"
                    else -> "O"
                }
            }.joinToString("  ")
        }.joinToString("\n")
        narrate("Current hero's position:")
        println(asciiMap)
    }

    fun ringBell() {
        if (currentRoom is TownSquare) {
            (currentRoom as TownSquare).ringBell()
        } else {
            narrate("You cannot ring bell here.\nYou need to go to the Town Square")
        }
    }

    private class GameInput(arg: String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }

        fun processCommand() = when (command.lowercase()) {
            "move" -> {
                val direction = Direction.values()
                    .firstOrNull { it.name.equals(argument, ignoreCase = true) }
                if (direction != null) {
                    move(direction)
                } else {
                    narrate("I don't know what direction that is")
                }
            }
            "map" -> {
                map()
            }
            "cast" -> {
                val spell = argument.lowercase()
                castSpell(spell)
            }
            "prophesize" -> {
                prophesize()
            }
            "ring" -> {
                ringBell()
            }
            "quit", "exit" -> {
                isLaunched = false
                narrate("Goodbye, Immortal ${player.name}, see you soon!")
            }
            else -> narrate("I'm not sure what you're trying to do")
        }
    }
}