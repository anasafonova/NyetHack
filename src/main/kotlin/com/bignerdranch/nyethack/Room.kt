package com.bignerdranch.nyethack

open class Room(val name: String) {
    protected open val status = "Calm"

    open fun description() = "$name (Currently: $status)"

    open fun enterRoom() {
        narrate("There's nothing to do here")
    }
}

open class MonsterRoom(
    name: String,
    var monster: Monster? = listOf(Goblin(), Draugr(), Werewolf(), Dragon()).random()
) : Room(name) {
    override fun description() =
        super.description() + " (Creature: ${monster?.description ?: "None"})"

    override fun enterRoom() {
        if (monster == null) {
            super.enterRoom()
        } else {
            narrate("Danger is lurking in this room")
        }
//        monster?.also { super.enterRoom() } ?: narrate("Danger is lurking in this room")
    }
}