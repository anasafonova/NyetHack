import kotlin.random.Random

var narrationModifier: (String) -> String = { it }

inline fun narrate(
    message: String,
    modifier: (String) -> String = { narrationModifier(it) }
) {
    println(modifier(message))
}

fun changeNarratorMood() {
    lateinit var mood: String
    lateinit var modifier: (String) -> String
    when (Random.nextInt(8)) {
        1 -> {
            mood = "loud"
            modifier = { message ->
                val numExclamationPoints = 3
                message.uppercase() + "!".repeat(numExclamationPoints)
            }
        }
        2 -> {
            mood = "tired"
            modifier = { message ->
                message.lowercase().replace(" ", "... ")
            }
        }
        3 -> {
            mood = "unsure"
            modifier = { message ->
                "$message?"
            }
        }
        4 -> {
            var narrationsGiven = 0
            mood = "like sending an itemized bill"
            modifier = { message ->
                narrationsGiven++
                "$message.\n(I have narrated $narrationsGiven things)"
            }
        }
        5 -> {
            mood = "lazy"
            modifier = { message ->
                message.take(message.length/2)
            }
        }
        6 -> {
            mood = "mysterious"
            val regex: Regex = """[LET]""".toRegex()
            modifier = { message ->
                message.uppercase().replace(regex) { m ->
                    when (m.value) {
                        "L" -> "1"
                        "E" -> "3"
                        "T" -> "7"
                        else -> ""
                    }
                }
            }
        }
        7 -> {
            mood = "poetic"
            val regex: Regex = """\s""".toRegex()
            modifier = { message ->
                message.replace(regex) { m ->
                    when (Random.nextBoolean()) {
                        true -> "\n"
                        false -> " "
                    }
                }
            }
        }
        else -> {
            mood = "professional"
            modifier = { message ->
                "$message."
            }
        }
    }

    narrationModifier = modifier
    narrate("The narrator begins to feel $mood")
}