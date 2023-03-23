package com.bignerdranch.nyethack

open class Room(val name: String) {
    protected open val status = "Calm"

    fun description() = "$name (Currently: $status)"

    open fun enterRoom() {
        narrate("There's nothing to do here")
    }
}