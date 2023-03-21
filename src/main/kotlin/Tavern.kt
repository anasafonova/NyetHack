import java.io.File
import kotlin.random.Random

private const val TAVERN_MASTER = "Jaeger"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq", "Dzaychok", "Dzayka")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider", "Dzaichkizm", "Nyaaa")

private val menuData = File("data/tavern-menu-data.txt")
    .readText()
    .split('\n')
    .sorted()
    .map { it.split(',') }

private val menuItems = menuData.map { (_, name, _) -> name }

private val menuItemPrices = menuData.associate { (_, name, price) -> name to price.toDouble() }

private val menuItemTypes = menuData.associate {  (type, name, _) -> name to type }

fun visitTavern() {
    narrate("${player.name} enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    println(menuItems.joinToString())

    val patrons: MutableSet<String> = firstNames.shuffled()
        .zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName" }
        .toMutableSet()

    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        player.name to 4.50,
        *patrons.map { it to 6.0 }.toTypedArray()
    )

    showMenu()

    narrate("${player.name} sees several patrons in the tavern:")
    narrate(patrons.joinToString())

    val itemOfTheDay = patrons.flatMap { getFavoriteMenuItems(it) }
        .fold(mutableMapOf<String,Int>()) { map, element ->
        map[element] = map.getOrDefault(element, 0) + 1
        map
    }.maxByOrNull { it.value }!!
        .key

//    val itemOfTheDay = patrons.flatMap { getFavoriteMenuItems(it) }.random()
    println("The item of the day is $itemOfTheDay")

    displayPatronBalances(patronGold)
    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }
    displayPatronBalances(patronGold)

    patrons
        .filter { patron -> patronGold.getOrDefault(patron, 0.0) < 4.0 }
        .also { departingPatrons ->
            patronGold -= departingPatrons.toSet()
            patrons -= departingPatrons.toSet()
        }.forEach {  patron ->
            narrate("${player.name} sees $patron departing the tavern")
        }

    narrate("There are still several patrons in the tavern:")
    narrate(patrons.joinToString())
}

private fun showMenu() {
    println()
    println("*** Welcome to $TAVERN_NAME ***")

    var lastType = ""

    menuData.forEachIndexed { index, _ ->
        val (type, name, price) = menuData[index]
        if (type != lastType) {
            val spacesNum = 17 - type.length / 2
            println("${" ".repeat(spacesNum)}~[$type]~")
            lastType = type
        }
        val pointsNum = 35 - name.length - price.length
        println("$name${".".repeat(pointsNum)}$price")
    }

    println()
}

private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemTypes[menuItem]?.contains("dessert") == true
        }
        else -> menuItems.shuffled().take(Random.nextInt(2) + 1)
    }
}

private fun placeOrder(
    patronName: String,
    menuItemName: String,
    patronGold: MutableMap<String, Double>
) {
    val itemPrice = menuItemPrices.getValue(menuItemName)

    narrate("$patronName speaks with $TAVERN_MASTER to place an order")

    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        val action = when (menuItemTypes[menuItemName]) {
            "shandy", "elixir" -> "pours"
            "meal" -> "serves"
            else -> "hands"
        }
        narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
        narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
        patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coins for a $menuItemName\"")
    }
}

fun displayPatronBalances(
    patronGold: Map<String, Double>
) {
    narrate("${player.name} intuitively knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}