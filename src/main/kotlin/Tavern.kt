import java.io.File

private const val TAVERN_MASTER = "Jaeger"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data.txt")
    .readText()
    .split('\n')
    .sorted()

private val menuItems = menuData.map { menuEntry: String ->
    val (_, name, _) = menuEntry.split(',')
    name
}

private val menuItemPrices = menuData.map {menuEntry: String ->
    val (_, name, price) = menuEntry.split(',')
    name to price.toDouble()
}.toMap()

private val menuItemTypes = menuData.map {menuEntry: String ->
    val (type, name, _) = menuEntry.split(',')
    name to type
}.toMap()

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    println(menuItems.joinToString())

    val patrons: MutableSet<String> = mutableSetOf()
    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50
    )

    while (patrons.size < 5) {
        val patronName = "${firstNames.random()} ${lastNames.random()}"
        patrons += patronName
        patronGold += patronName to 6.0
    }

    showMenu()

    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())

    displayPatronBalances(patronGold)
    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }
    displayPatronBalances(patronGold)
}

private fun showMenu() {
    println()
    println("*** Welcome to $TAVERN_NAME ***")

    var lastType = ""

    menuData.sorted().forEachIndexed { index, row ->
        val (type, name, price) = menuData[index].split(',')
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
    narrate("$heroName intuitively knows how much money each patron has")
    patronGold.forEach { patron, balance ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}